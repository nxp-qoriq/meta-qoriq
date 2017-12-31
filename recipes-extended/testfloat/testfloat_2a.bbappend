DEPENDS += "dos2unix-native"

do_unpack2_prepend () {
    #mv ${WORKDIR}/TestFloat-2a/SoftFloat-2b ${WORKDIR}/SoftFloat-2b
}

addtask do_unpack2 after do_prepare_recipe_sysroot do_unpack before do_patch
