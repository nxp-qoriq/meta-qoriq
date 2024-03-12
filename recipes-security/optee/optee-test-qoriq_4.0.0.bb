# Copyright 2022-2024 NXP
require optee-test-qoriq.inc

OPTEE_TEST_BRANCH = "lf-6.6.3_1.0.0"
SRCREV = "95c49d950f50fa774e4530d19a967079b3b61279"

PV:append = "+git${SRCPV}"
