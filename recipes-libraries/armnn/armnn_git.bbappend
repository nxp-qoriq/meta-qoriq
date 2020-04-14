PACKAGECONFIG_OPENCL_qoriq-arm64   = "opencl"

PACKAGECONFIG ??= "neon caffe tensorflow tensorflow_lite onnx unit_tests"
COMPATIBLE_MACHINE = "(mx8|qoriq-arm64)"
