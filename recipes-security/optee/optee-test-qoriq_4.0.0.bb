# Copyright 2022-2023 NXP
require optee-test-qoriq.inc

OPTEE_TEST_BRANCH = "lf-6.1.55_2.2.0"
SRCREV = "38efacef3b14b32a6792ceaebe211b5718536fbb"

PV:append = "+git${SRCPV}"
