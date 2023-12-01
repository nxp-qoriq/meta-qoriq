# Copyright 2022-2023 NXP
require optee-client-qoriq.inc

OPTEE_CLIENT_BRANCH = "lf-6.1.55_2.2.0"
SRCREV = "acb0885c117e73cb6c5c9b1dd9054cb3f93507ee"

PV:append = "+git${SRCPV}"
