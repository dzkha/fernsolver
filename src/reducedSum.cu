/* 
 * Example of using reducing (tree) type algorithms to parallelize finding the sum of
 * a set of numbers. On a GF 8600 GT the two parallel algorithms (sumControl = 0 or 1) 
 * are about 35 times faster than the serial algorithm also running on the GPU but using 
 * global memory (sumControl=2), for an array of 512 floats.  This is both because
 * the parallel algorithms scale as ln N while the serial algorithm scales as N, and because the
 * parallel algorithms use the shared memory while the serial one uses the (generally slower)
 * global memory in these tests. If we do the serial algorithm on the same computer but use
 * shared memory (set sumControl=3), the parallel algorithms are only 3.5 times faster.  Thus a
 * factor of 10 in the speed up is because of using the shared memory in this example. Note, 
 * however, that shared memory can only be shared among the threads within a single block.
 * 
 */

#include <stdio.h>

#define BLOCKSIZE 512 

// Define some GPU timing utilities. These are invoked from the host program. Usage:
//     START_GPU;
//         kernelFunction <<< numBlocks, threadsPerBlock >>> (args)
//     STOP_GPU;
//     PRINT_GPU
// in the host code. This estimates the time for the kernel kernelFunction to run on the GPU.
// For a more extensive discusion, see Section 5.1.2 of the CUDA Best Practices Guide at
// http://developer.download.nvidia.com/compute/DevZone/docs/html/C/doc/CUDA_C_Best_Practices_Guide.pdf

float timeGPU;
cudaEvent_t start, stop;
#define START_GPU cudaEventCreate(&start); cudaEventCreate(&stop); cudaEventRecord(start, 0);
#define STOP_GPU cudaEventRecord(stop, 0); cudaEventSynchronize(stop);\
   cudaEventElapsedTime(&timeGPU, start, stop);\
   cudaEventDestroy(start);cudaEventDestroy(stop);
#define PRINT_GPU printf("\n\nTime to compute on GPU: %f ms \n", timeGPU);

// Define a utility to check for CUDA errors.  Place it immediately after a CUDA kernel
// call in the host code. The initial cudaDeviceSynchronize() command ensures that the device
// has completed all preceding requested tasks.

#define CUDA_ERROR_CHECK cudaDeviceSynchronize(); cudaError_t error = cudaGetLastError();\
   if(error != cudaSuccess){printf("***CUDA error: %s\n", cudaGetErrorString(error)); exit(-1);}\
   else{printf("\nNo CUDA errors detected\n" );}
   

// Device code.  Sums the elements of the array Array and puts the result in Sum

__global__ void SumKernel(float* Array, float* Sum, int arraySize)
{
	__device__ float reductionSum(int, float *);

	*Sum = reductionSum(arraySize, Array);
}

/*
  Function to do parallel reduction sum. This should scale as ln N.  The parallel butterfly
  algorithm taken from the literature works generally.  My homegrown parallel version
  works as written for even number of entries in the array, so this algorithm can be used 
  for an odd number by padding the array with an added zero entry. Note that this version 
  assumes that all summations are within one block, so a max of 512 threads on 1.1 devices
  (presently blocksize is set to 256).  One option for larger sums is to break the array up 
  onto multiple blocks, use this algorithm on each block to get a block sum, and then sum
  the block sums.
*/

__device__ float reductionSum(int length, float *array)
{
	float sum = 0.0f;
	
	// = 0 or 1 for parallel with shared memory, 2 for serial with global, 3 for serial with shared
	int sumControl = 0;
	
	// Copy the array to be summed into shared memory and initialize
	__shared__ float sarray[BLOCKSIZE];
	int i = threadIdx.x;
	sarray[i] = 0.0f;
	if(i<length) sarray[i] = array[i];
	__syncthreads();
	
	
	if(sumControl == 0)
	{
		// Parallel butterfly sum
		// see http://cs.anu.edu.au/files/systems/GPUWksp/PDFs/02_CUDAParallelProgrammingModel.pdf
		
		for(int bit=BLOCKSIZE/2; bit>0; bit /= 2)
		{
			if(i<length)
			{
				float t=sarray[i] + sarray[i^bit];
				__syncthreads();
				sarray[i] = t;
				__syncthreads();
			}
		}
		
		// The array entries sarray[i] with i<length/2 now all contain the sum
		
		sum = sarray[0];
	
	}
	else if(sumControl == 1)
	{
		// Another home-made parallel version of a reduction sum. As written, this requires an even
		// number of entries in the array to be summed, so pad with a zero to handle odd number
		// (or rewrite to deal with odd number).
		
		int L=length;
		int steps = 0;
		int inc = 1;
		float t = 0;
		
		while(L > 2 )
		{
			steps ++;
			if(i < (length-inc))
				t = sarray[i] + sarray[i+inc];
				__syncthreads();
				sarray[i] = t;
				__syncthreads();
			inc *= 2;
			L /= 2;
		}
		
		sum = sarray[0] + sarray[inc];  // This contains the sum
	}
	else if(sumControl == 2)
	{	
		// Serial version of sum accessing global (not shared) memory
	
		sum = 0.0f;
		for(int i=0; i<length; i++)
		{
			sum += array[i];
		}
	}
	else
	{
		// Serial version of sum accessing shared memory
	
		sum = 0.0f;
		for(int i=0; i<length; i++)
		{
			sum += sarray[i];
		}
	}
	
	return sum;
}



// Host code
int main(void)
{
	
	int arraySize = 512;
	float* Array;
	
	Array = (float*) malloc(sizeof(float) * arraySize);
	
	// Fill array with some numbers
	
	for(int i=0; i<arraySize; i++)
	{
		Array[i] = 2.0f * (float) i;
	}
	
	// Set up device pointers
	float *devPtrArray;
	
	float Sum;
	float* devPtrSum;                            
	cudaMalloc((void**)&devPtrSum, sizeof(float));
	
	// Allocate device memory
	cudaMalloc((void**)&devPtrArray, arraySize * sizeof(float));
	
	// Copy array to device
	cudaMemcpy(devPtrArray, Array, arraySize * sizeof(float), cudaMemcpyHostToDevice);
	
	// Launch the kernel.
	
	START_GPU;     // Start timer for device code
	
	SumKernel<<<1, 512>>>(devPtrArray, devPtrSum, arraySize);
	
	STOP_GPU;           // Stop timer for device code
	PRINT_GPU;          // Print timing for device code
	CUDA_ERROR_CHECK
	
	// Copy the sum back from the GPU to the host
	
	cudaMemcpy(&Sum, devPtrSum, sizeof(float), cudaMemcpyDeviceToHost);
	
	printf("\nSum array elements multiplied by 2 (GPU) = %6.2f\n\n", Sum);
	 
    // Free the memory allocated on the device   
	cudaFree(devPtrSum);
	cudaFree(devPtrArray);
	
	// Free the memory allocated on the CPU
	free(Array);
	
    return 0;
	
}

