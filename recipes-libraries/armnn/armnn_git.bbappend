ARMNN_SRC = "git://bitbucket.sw.nxp.com/aitec/armnn-imx.git;protocol=ssh"

PACKAGECONFIG_OPENCL_qoriq-arm64   = "opencl"

PACKAGECONFIG ??= "neon caffe tensorflow tensorflow_lite onnx unit_tests"
COMPATIBLE_MACHINE = "(mx8|qoriq-arm64)"
