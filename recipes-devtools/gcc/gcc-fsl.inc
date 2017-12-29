FILESEXTRAPATHS_prepend := "${THISDIR}/gcc-4.9:"

NATIVEDEPS_append_qoriq-ppc = " cloog-native"
EXTRA_OECONF_BASE_remove_qoriq-ppc = "--with-cloog=no"

PV = "4.9.2"

# BINV should be incremented to a revision after a minor gcc release
BINV = "4.9.2"

SRC_URI_qoriq-ppc = "\
    ${GNU_MIRROR}/gcc/gcc-${PV}/gcc-${PV}.tar.bz2 \
    file://0001.gcc.e6500-FSF-49x.patch \
    file://0002.gcc.builtin_isel-49x.patch \
    file://0003.gcc.widen_types-49x.patch \
    file://0004.gcc.extelim-v4-49x.patch \
    file://0005.gcc.extelim_vrp_kugan-v1-49x.patch \
    file://0006.gcc.opt-array-offset-49x.patch \
    file://0007.gcc.aeabi-49x.patch \
    file://0008.gcc.fix_regalloc_for_482.patch \
    file://0009.gcc.rm_slow_tests-47.patch \
    file://0010.gcc.fix_mingw32.patch \
    file://0011.gcc.no_power_builtins-48.patch \
    file://0012.gcc.ld_unaligned-460.patch \
    file://0013.gcc.local_unaligned_altivec.patch \
    file://0014.gcc.soft_float-470.patch \
    file://0015.gcc.case_values-48.patch \
    file://0016.gcc.fix_pr63854_pass_manager.patch \
    file://0017.gcc.builtin_isel_doc.patch \
    file://0018.gcc.experimental_move.patch \
    file://0019.gcc.e5500_mfocr.patch \
    file://0020.gcc.load_on_store_bypass-48x.patch \
    file://0021.gcc.fix_constvector.patch \
    file://0022.gcc.fix_pr63908_unwind_info.patch \
    file://0023.gcc.fix_pr60158_fixup_table.patch \
    file://0024.gcc.have-pre-modify-disp-support-49x.patch \
    file://0025.gcc.fix_ENGR00298583_dwarf-vector-reg_49x.patch \
    file://0026.gcc.fix_MTWX51605-memset-array-init_48.patch \
    file://0027.gcc.fix_altivec_constant_alignment-v2.patch \
    file://0028.gcc.fix_altivec_reload_gs8.patch \
    file://0029.gcc.fix_postfix_gimplifier.patch \
    file://0030.gcc.fix_adjust_address_cost.patch \
    file://0031.gcc.fix_adjust_sched_loopinv_cost.patch \
    file://0032.gcc.fix_e5500_mulli_pipeline.patch \
    file://0033.gcc.fix_e500mc_addi_pipeline.patch \
    file://0034.gcc.fix_ENGR00292364_debug_frame.patch \
    file://0035.gcc.fix_ENGR00215936_49x.patch \
    file://0036.gcc.enable_soft_multilib-49x.patch \
    file://0037.gcc.fix_49x-doc.patch \
    file://0038.gcc.fix_emulation_spec_48.patch \
    file://0039.gcc.create_maeabi.patch \
    file://0040.gcc.rm_e500v2_loops_48.patch \
    file://0041.gcc.fix_e5500-e6500-aeabi-multi-lib.patch \
    file://0042.gcc.fix_ivopts.patch \
    file://0043.gcc.sysroot_spec_only_linux.patch \
    file://0019-64-bit-multilib-hack.patch \
    file://0020-optional-libstdc.patch \
    file://0023-Use-the-defaults.h-in-B-instead-of-S-and-t-oe-in-B.patch \
    file://0029-Define-GLIBC_DYNAMIC_LINKER-and-UCLIBC_DYNAMIC_LINKE.patch \
    file://0032-libtool.patch \
    file://0034-Use-the-multilib-config-files-from-B-instead-of-usin.patch \
    file://0040-fix-g++-sysroot.patch \
    file://0041-libtool-avoid-libdir.patch \
    file://0046-libatomic-deptracking.patch \
    file://0049-Enable-SPE-AltiVec-generation-on-powepc-linux-target.patch \
    file://0054-gcc-Makefile.in-fix-parallel-building-failure.patch \
    file://0066-cxxflags-for-build.patch \
"
SRC_URI[md5sum] = "4df8ee253b7f3863ad0b86359cd39c43"
SRC_URI[sha256sum] = "2020c98295856aa13fda0f2f3a4794490757fc24bcca918d52cc8b4917b972dd"
