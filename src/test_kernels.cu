
#include <explicitgpuDouble.cuh>


void launchTestReduce()
{
	unsigned short a_length = 1024;
	size_t a_size = sizeof(double) * a_length;
	double *a = (double *) malloc(a_size);
	
	for (int i = 0; i < a_length; i++)
		a[i] = (double) i;
	
	double *devA;
	cudaMalloc(&devA, a_size);
	cudaMemcpy(devA, a, a_size, cudaMemcpyHostToDevice);
	
	dim3 blocks(1);
	dim3 threads(512);
	testReduce<<<blocks, threads, a_size>>>(devA, a_length);
	CUDA_ERROR_CHECK
	
	cudaMemcpy(a, devA, a_size, cudaMemcpyDeviceToHost);
	printf("Sum: %f\n", a[0]);
}


__global__ void testReduce(double *a, unsigned short length)
{
	const int tid = threadIdx.x;
	double *as = (double *) dsmem;
	
	__syncthreads();
	
	// Perform reductions on the array
	unsigned int loops = 1u << 17;
	for (unsigned int loop = 0; loop < loops; loop++)
	{
		// Copy global array to shared
		for (int i = tid; i < length; i += blockDim.x)
			as[i] = a[i];
		
		__syncthreads();
		reduceSum(as, length);
		__syncthreads();
	}
	
	// Copy shared array to global
	for (int i = tid; i < length; i += blockDim.x)
		a[i] = as[i];
}


