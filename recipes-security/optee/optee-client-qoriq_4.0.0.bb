# Copyright 2022-2024 NXP
require optee-client-qoriq.inc

OPTEE_CLIENT_BRANCH = "lf-6.6.3_1.0.0"
SRCREV = "acb0885c117e73cb6c5c9b1dd9054cb3f93507ee"

PV:append = "+git${SRCPV}"
