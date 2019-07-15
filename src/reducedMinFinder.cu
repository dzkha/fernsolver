/* 
 * Example of using reducing (tree) type algorithms to parallelize finding the minimum of
 * a set of numbers. On a GF 8600 GT the two parallel algorithms (sumControl = 0 or 1) 
 * are about 35 times faster than the serial algorithm also running on the GPU but using 
 * global memory (sumControl=2), for an array of 512 floats.  This is both because
 * the parallel algorithms scale as ln N while the serial algorithm scales as N, and because the
 * parallel algorithms use the shared memory while the serial one uses the (generally slower)
 * global memory in these tests. If we do the serial algorithm on the same computer but use
 * shared memory (set sumControl=3), the parallel algorithms are only 4.5 times faster.  Thus a
 * factor of around 10 in the speed up is because of using the shared memory in this example. Note, 
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
   

// Device code.  Examines the elements of the array Array and puts the min value in MinVal

__global__ void MinValKernel(float* Array, float* MinVal, int arraySize)
{
	__device__ float reductionMinVal(int, float *);

	*MinVal = reductionMinVal(arraySize, Array);
}

/*
  Function to do parallel reduction minVal. This should scale as ln N.  The parallel butterfly
  algorithm taken from the literature works generally.  My homegrown parallel version
  works as written for even number of entries in the array, so this algorithm can be used 
  for an odd number by padding the array with an added zero entry. Note that this version 
  assumes that all minVals are within one block, so a max of 512 threads on 1.1 devices
  (presently blocksize is set to 512).  One option for larger minVals is to break the array up 
  onto multiple blocks, use this algorithm on each block to get a block minVal, and then minVal
  the block minVals.
*/

__device__ float reductionMinVal(int length, float *array)
{
	float minVal;
	
	// = 0 or 1 for parallel with shared memory, 2 for serial with global, 3 for serial with shared
	int minValControl = 3;
	
	// Copy the array to be minValed into shared memory and initialize
	__shared__ float sarray[BLOCKSIZE];
	int i = threadIdx.x;
	sarray[i] = 0.0f;
	if(i<length) sarray[i] = array[i];
	__syncthreads();
	
	
	if(minValControl == 0)
	{
		// Parallel butterfly minVal
		// see http://cs.anu.edu.au/files/systems/GPUWksp/PDFs/02_CUDAParallelProgrammingModel.pdf
		
		for(int bit=BLOCKSIZE/2; bit>0; bit /= 2)
		{
			if(i<length)
			{
				float t = sarray[i];
				if(sarray[i^bit] < t) t = sarray[i^bit];
				__syncthreads();
				sarray[i] = t;
				__syncthreads();
			}
		}
		
		minVal = sarray[0];   // Contains the min value
	
	}
	else if(minValControl == 1)
	{
		// Another home-made parallel version of a reduction minVal. As written, this requires an even
		// number of entries in the array to be minValed, so pad with a zero to handle odd number
		// (or rewrite to deal with odd number).
		
		int L=length;
		int steps = 0;
		int inc = 1;
		float t = 0;
		
		while(L > 2 )
		{
			steps ++;
			if(i < (length-inc))
				t = sarray[i];
				if(sarray[i+inc] < t) t = sarray[i+inc];
				__syncthreads();
				sarray[i] = t;
				__syncthreads();
			inc *= 2;
			L /= 2;
		}
		
		minVal = sarray[0];   // This contains the minVal
	}
	else if(minValControl == 2)
	{	
	// For reference: Serial version of minimum finder running on the GPU and accessing global 
	// (not shared) memory.
	
		minVal = 1.0e+20;
		for(int i=0; i<length; i++)
		{
			if(array[i] < minVal)
			{
				minVal = array[i];
			}
		}
	}
	else
	{
		// For reference: Serial version of minimum finder running on the GPU and accessing shared 
		// memory.
		
		minVal = 1.0e+20;
		for(int i=0; i<length; i++)
		{
			if(sarray[i] < minVal)
			{
				minVal = sarray[i];
			}
		}
	}
	
	return minVal;
}



// Host code running on CPU
int main(void)
{
	int arraySize = 512;
	float* Array;
	float MinVal;
	
	Array = (float*) malloc(sizeof(float) * arraySize);
	
	// Fill array with some numbers.  Make it a monotonic function with minimum at
	// i = 300.  Then add some negative spikes to it so there are multiple local
	// minima.
	
	for(int i=0; i<arraySize; i++)
	{
		Array[i] = 0.005f*(float)(i-200)*(i-200) +13.0f;
	}
	
	// Add some negative spikes so that min is at i=100, corresponding to a value -39.6.
	Array[100] = -39.6f;
	Array[150] = 15.0f;
	Array[400] = 26.8f;
	Array[450] = -30.0f;
	
	// Set up device pointers
	float *devPtrArray;
	float* devPtrMinVal; 
	
	// Allocate device memory
	cudaMalloc((void**)&devPtrMinVal, sizeof(float));
	cudaMalloc((void**)&devPtrArray, arraySize * sizeof(float));
	
	// Copy array to device
	cudaMemcpy(devPtrArray, Array, arraySize * sizeof(float), cudaMemcpyHostToDevice);
	
	// Launch the kernel.
	
	START_GPU;     // Start timer for device code
	
	MinValKernel<<<1, 512>>>(devPtrArray, devPtrMinVal, arraySize);
	
	STOP_GPU;           // Stop timer for device code
	PRINT_GPU;          // Print timing for device code
	CUDA_ERROR_CHECK
	
	// Copy the minVal back from the GPU to the host
	
	cudaMemcpy(&MinVal, devPtrMinVal, sizeof(float), cudaMemcpyDeviceToHost);
	
	// Print out all the values in the array
	for(int i=0; i< arraySize; i++)
	{
		printf("%d %8.3e\n", i, Array[i]);
	}
	
	printf("\nTime to compute on GPU: %f ms \n", timeGPU);
	
	printf("\nMin value in array (GPU) = %6.2f\n\n", MinVal);
	 
    // Free the memory allocated on the device   
	cudaFree(devPtrMinVal);
	cudaFree(devPtrArray);
	
	// Free the memory allocated on the CPU
	free(Array);
	
    return 0;
	
}
