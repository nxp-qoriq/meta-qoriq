DEPENDS += "dos2unix-native"

addtask do_unpack2 after do_prepare_recipe_sysroot do_unpack before do_patch
