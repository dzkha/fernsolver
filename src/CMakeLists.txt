# Include the FindCuda script

include(FindCUDA)

# Set the default compute capability version
# Run CMake with -DCUDA_COMPUTE_CAPABILITY=30 to compile
# with capability 3.0, for example.

if(NOT DEFINED CUDA_COMPUTE_CAPABILITY)
	set(CUDA_COMPUTE_CAPABILITY 20)
endif()

set(CUDA_NVCC_FLAGS ${CUDA_NVCC_FLAGS};-arch sm_${CUDA_COMPUTE_CAPABILITY};--ptxas-options=-v)

set(SOURCES
	main.cu
	FERNIntegrator.cu
	Network.cu
	Globals.cu
	IntegrationData.cu
	kernels.cu
)

# Compile the parallel kernel

CUDA_ADD_EXECUTABLE(fern ${SOURCES})
