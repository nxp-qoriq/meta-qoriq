#!/bin/bash

# BSD LICENSE
#
# Copyright 2017,2022 NXP
#
#

Usage()
{
    echo "Usage: $0 -m MACHINE  -t BOOTTYPE -d TOPDIR -s DEPLOYDIR -e ENCAP -i IMA_EVM -o SECURE\

        -m        machine name
        -t        boottype
        -d        topdir
        -s        deploy dir
        -o   secure
"
    exit
}

# get command options
while getopts "m:t:d:s:o:" flag
do
        case $flag in
                m) MACHINE="$OPTARG";
                   echo "machine: $MACHINE";
                   ;;
                t) BOOTTYPE="$OPTARG";
                   echo "secure boot type: $BOOTTYPE";
                   ;;
                d) TOPDIR="$OPTARG";
                   echo "top dir : $TOPDIR";
                   ;;
                s) DEPLOYDIR="$OPTARG";
                   echo "deploy dir : $DEPLOYDIR";
                   ;;
                o) SECURE="$OPTARG";
                   echo "secure : $SECURE";
                   ;;
                ?) Usage;
                   exit 3
                   ;;
        esac
done

secure_sign_image() {
    echo "Signing $2boot images for $1 ..."

    . $1.manifest

    secureboot_headers=`eval echo '${'"secureboot_headers_""$2"'}'`
    [ -z "$secureboot_headers" ] && echo ${2}boot secure on $1: unsupported && return

    rm -f bootscript uImage.dtb uImage.bin kernel.itb secboot_hdrs*.bin hdr*.out
    cp $DEPLOYDIR/$distro_bootscript $TOPDIR/bootscript && echo "Copying bootscript"
    cp $DEPLOYDIR/$device_tree $TOPDIR/uImage.dtb && echo "Copying dtb"
    cp $DEPLOYDIR/$kernel_img $TOPDIR/uImage.bin && echo "Copying kernel"
    cp $DEPLOYDIR/$kernel_itb $TOPDIR/kernel.itb && echo "Copying kernel_itb"

    rcwimg_nonsec=`eval echo '${'"rcw_""$2"'}'`
    if [ -z "$rcwimg_nonsec" ]; then
        echo ${2}boot on $1 is not supported && return
    fi

    if [ "${1:0:7}" = "ls1021a" ]; then
        ubootimg_sec=`eval echo '${'"uboot_""$2"'boot_sec}'`
        if [ -z "$ubootimg_sec" ]; then
            echo $2 boot on $1 for secureboot unsupported
            return
        fi
        if [ "$2" = "nor" -o "$2" = "qspi" ]; then
            cp $DEPLOYDIR/$ubootimg_sec $TOPDIR/u-boot-dtb.bin
        elif [ "$2" = "sd" -o "$2" = "emmc" ]; then
            if [ -z "$uboot_sdboot_sec" ]; then
                echo $2 boot on  for secureboot unsupported
                return
            fi
            cp $DEPLOYDIR/$uboot_sdboot_sec $TOPDIR/u-boot-with-spl-pbl.bin
            cp $DEPLOYDIR/$uboot_spl $TOPDIR/u-boot-spl.bin
            cp $DEPLOYDIR/$uboot_dtb $TOPDIR/u-boot-dtb.bin
        fi
    fi

    if [ -f $DEPLOYDIR/$pfe_fw ]; then
        cp $DEPLOYDIR/$pfe_fw $TOPDIR/pfe.itb && echo "Copying PFE"
        pfe_fw=pfe.itb
    fi
     
    if [ -f $DEPLOYDIR/$dpaa2_mc_fw ]; then
        cp $DEPLOYDIR/$dpaa2_mc_fw $TOPDIR/mc.itb
    fi

    if [ -f $DEPLOYDIR/$dpaa2_mc_dpc ]; then
        cp $DEPLOYDIR/$dpaa2_mc_dpc $TOPDIR/dpc.dtb
    fi

    if [ -f $DEPLOYDIR/$dpaa2_mc_dpl ]; then
        cp $DEPLOYDIR/$dpaa2_mc_dpl $TOPDIR/dpl.dtb
    fi

    if [ ! -d  $DEPLOYDIR/secboot_hdrs/ ]; then
        mkdir -p  $DEPLOYDIR/secboot_hdrs/
    fi
    if [ "$2" = "nand" -a -n "$nand_script" ]; then
        . $nand_script
    elif [ "$2" = "sd" -o "$2" = "emmc" ] && [ -n "$sd_script" ]; then
        . $sd_script
    elif [ "$2" = "nor" -a -n "$nor_script" ]; then
        . $nor_script
    elif [ "$2" = "qspi" -a -n "$qspi_script" ]; then
        . $qspi_script
    elif [ "$2" = "xspi" -a -n "$xspi_script" ]; then
        . $xspi_script
    fi

    if [ "${1:0:7}"  = "ls1028a" ]; then
        cp $TOPDIR/secboot_hdrs.bin $DEPLOYDIR/secboot_hdrs/secboot_hdrs_${2}boot.bin
    else
        cp $TOPDIR/secboot_hdrs_${2}boot.bin $DEPLOYDIR/secboot_hdrs/
    fi
    if [ "${1:0:7}" = "ls1021a" -a "$2" != "nor" ]; then
        cp -f $TOPDIR/u-boot-with-spl-pbl-sec.bin $DEPLOYDIR/$uboot_sdboot_sec
    fi
    cp $TOPDIR/hdr_dtb.out $DEPLOYDIR/secboot_hdrs/
    cp $TOPDIR/hdr_linux.out $DEPLOYDIR/secboot_hdrs/
    if [ "$1"  = "ls1012afrwy" ]; then
        cp $TOPDIR/hdr_kernel.out $DEPLOYDIR/secboot_hdrs/
    fi

    cp $TOPDIR/hdr_bs.out $DEPLOYDIR/secboot_hdrs/hdr_${1}_bs.out
    cp $TOPDIR/srk_hash.txt $DEPLOYDIR/
    cp $TOPDIR/srk.pri $DEPLOYDIR/
    cp $TOPDIR/srk.pub $DEPLOYDIR/
}


generate_qoriq_composite_firmware() {
    # generate machine-specific firmware to be programmed to NOR/SD media
    # $1: machine name
    # $2: boot type: sd, qspi, xspi, nor, nand

    . $1.manifest
    . memorylayout.cfg
    if [ "$SECURE" = "true" -a $2 != "emmc" ]; then
      fwimg=$DEPLOYDIR/firmware_${1}_uboot_${2}boot_secure
      rcwimg=`eval echo '${'"rcw_""$2"'_sec}'`
      bootloaderimg=`eval echo '${'"uboot"'_'"$2"'boot_sec}'`
      bl2img=`eval echo '${'"atf_bl2_""$2"'_sec}'`
      fipimg=`eval echo '${'"atf_fip_""uboot"'_sec}'`
      secureboot_headers=`eval echo '${'"secureboot_headers_""$2"'}'`
    else
      fwimg=$DEPLOYDIR/firmware_${1}_uboot_${2}boot
      rcwimg=`eval echo '${'"rcw_""$2"'}'`
      bootloaderimg=`eval echo '${'"uboot"'_'"$2"'boot}'`
      bl2img=`eval echo '${'"atf_bl2_""$2"'}'`
      fipimg=`eval echo '${'"atf_fip_""uboot"'}'`
    fi
    if [ -f $fwimg ]; then
        rm -f $fwimg
    fi

    if [ "${1:0:7}" = "ls1021a" ]; then
        # for machine which doesn't support ATF
        if [ "$2" = "sd" -o "$2" = "emmc" ]; then
            # rcw and uboot/uefi in single image
            dd if=$DEPLOYDIR/$bootloaderimg of=$fwimg bs=512 seek=$sd_rcw_bootloader_offset
        else
            # program rcw  ls1021atwr secure qspi unsupported
            if [ -z "$rcwimg" ]; then echo ${2}boot on ${1} is not unsupported!; return; fi
            dd if=$DEPLOYDIR/$rcwimg of=$fwimg bs=1K seek=0
            # program u-boot image
            val=`expr $(echo $(($nor_bootloader_offset))) / 1024`
            dd if=$DEPLOYDIR/$bootloaderimg of=$fwimg bs=1K seek=$val
        fi
    else
        # ATF BL2 image
        if [ "$2" = "sd" -o "$2" = "emmc" ]; then
            dd if=$DEPLOYDIR/$bl2img of=$fwimg bs=512 seek=$sd_rcw_bootloader_offset
        else
            dd if=$DEPLOYDIR/$bl2img of=$fwimg bs=1K seek=0
        fi

        # ATF FIP image
        if [ "$2" = "sd" -o "$2" = "emmc" ]; then
            dd if=$DEPLOYDIR/$fipimg of=$fwimg bs=512 seek=$sd_bootloader_offset
        else
            val=`expr $(echo $(($nor_bootloader_offset))) / 1024`
            dd if=$DEPLOYDIR/$fipimg of=$fwimg bs=1K seek=$val
        fi
    fi
    # secure boot headers
    if [ -n "$secureboot_headers" -a "$SECURE" = "true" ]; then
        if [ "$2" = "nor" -o "$2" = "qspi" -o "$2" = "xspi" -o "$2" = "nand" ]; then
            val=`expr $(echo $(($nor_secureboot_headers_offset))) / 1024`
            dd if=$DEPLOYDIR/$secureboot_headers of=$fwimg bs=1K seek=$val
        elif [ "$2" = "sd" -o "$2" = "emmc" ]; then
            dd if=$DEPLOYDIR/$secureboot_headers of=$fwimg bs=512 seek=$sd_secureboot_headers_offset
        fi
    fi

    # DDR PHY firmware
    if [ "${1:0:7}" = "lx2160a" -o "${1:0:7}" = "lx2162a" ]; then
        if [ "$SECURE" = "true" ]; then
            ddrphyfw=$ddr_phy_fw_sec
        else
            ddrphyfw=$ddr_phy_fw
        fi
        if [ "$2" = "nor" -o "$2" = "qspi" -o "$2" = "xspi" -o "$2" = "nand" ]; then
            val=`expr $(echo $(($nor_ddr_phy_fw_offset))) / 1024`
            dd if=$DEPLOYDIR/$ddrphyfw of=$fwimg bs=1K seek=$val
        elif [ "$2" = "sd" -o "$2" = "emmc" ]; then
            dd if=$DEPLOYDIR/$ddrphyfw of=$fwimg bs=512 seek=$sd_ddr_phy_fw_offset
        fi
    fi
     # fuse provisioning in case CONFIG_FUSE_PROVISIONING is enabled
    if [ "$CONFIG_FUSE_PROVISIONING" = "y" -a "${1:0:7}" != "ls1021a" ]; then
        if [ "$SECURE" = "true" ]; then
            fuse_header=$DEPLOYDIR/atf/fuse_fip_sec.bin
        else
            fuse_header=$DEPLOYDIR/atf/fuse_fip.bin
        fi
        if [ "$2" = "nor" -o "$2" = "qspi" -o "$2" = "xspi" -o "$2" = "nand" ]; then
            val=`expr $(echo $(($nor_fuse_headers_offset))) / 1024`
            dd if=$fuse_header of=$fwimg bs=1K seek=$val
        elif [ "$2" = "sd" -o "$2" = "emmc" ]; then
            dd if=$fuse_header of=$fwimg bs=512 seek=$sd_fuse_headers_offset
        fi
    fi

    # DPAA1 FMan ucode firmware
    if [ -n "$fman_ucode" ]; then
        if [ "$2" = "nor" -o "$2" = "qspi" -o "$2" = "xspi" -o "$2" = "nand" ]; then
            val=`expr $(echo $(($nor_fman_ucode_offset))) / 1024`
            dd if=$DEPLOYDIR/$fman_ucode of=$fwimg bs=1K seek=$val
        elif [ "$2" = "sd" -o "$2" = "emmc" ]; then
            dd if=$DEPLOYDIR/$fman_ucode of=$fwimg bs=512 seek=$sd_fman_ucode_offset
        fi
    fi
    # QE/uQE firmware
    if [ -n "$qe_firmware" ]; then
        if [ "$2" = "nor" -o "$2" = "qspi" -o "$2" = "xspi" -o "$2" = "nand" ]; then
            val=`expr $(echo $(($nor_qe_firmware_offset))) / 1024`
            dd if=$DEPLOYDIR/$qe_firmware of=$fwimg bs=1K seek=$val
        elif [ "$2" = "sd" -o "$2" = "emmc" ]; then
            dd if=$DEPLOYDIR/$qe_firmware of=$fwimg bs=512 seek=$sd_qe_firmware_offset
        fi
    fi

    # DP (Display Port) firmware
    if [ -n "$dp_firmware" ] && [ "${1:0:7}" = "ls1028a" ]; then
        if [ "$2" = "nor" -o "$2" = "qspi" -o "$2" = "xspi" -o "$2" = "nand" ]; then
            val=`expr $(echo $(($nor_dp_firmware_offset))) / 1024`
            dd if=$DEPLOYDIR/$dp_firmware of=$fwimg bs=1K seek=$val
        elif [ "$2" = "sd" -o "$2" = "emmc" ]; then
            dd if=$DEPLOYDIR/$dp_firmware of=$fwimg bs=512 seek=$sd_dp_firmware_offset
	fi
    fi

    # ethernet phy firmware
    if [ -n "$phy_firmware" ]; then
        if [ "$2" = "nor" -o "$2" = "qspi" -o "$2" = "xspi" -o "$2" = "nand" ]; then
            val=`expr $(echo $(($nor_phy_firmware_offset))) / 1024`
            dd if=$DEPLOYDIR/$phy_firmware of=$fwimg bs=1K seek=$val
        elif [ "$2" = "sd" -o "$2" = "emmc" ]; then
            dd if=$DEPLOYDIR/$phy_firmware of=$fwimg bs=512 seek=$sd_phy_firmware_offset
        fi
    fi

    # DPAA2-MC or PFE firmware
    if [ -n "$dpaa2_mc_fw" ]; then
        fwbin=`ls $DEPLOYDIR/$dpaa2_mc_fw`
    elif [ -n "$pfe_fw" ]; then
        fwbin=$DEPLOYDIR/$pfe_fw
    fi
    if [ -n "$fwbin" ]; then
        if [ "$2" = "nor" -o "$2" = "qspi" -o "$2" = "xspi" -o "$2" = "nand" ]; then
            val=`expr $(echo $(($nor_dpaa2_mc_fw_offset))) / 1024`
            dd if=$fwbin of=$fwimg bs=1K seek=$val
        elif [ "$2" = "sd" -o "$2" = "emmc" ]; then
            dd if=$fwbin of=$fwimg bs=512 seek=$sd_dpaa2_mc_fw_offset
        fi
    fi

    # DPAA2 DPL firmware
    if [ -n "$dpaa2_mc_dpl" ]; then
        if [ "$2" = "nor" -o "$2" = "qspi" -o "$2" = "xspi" -o "$2" = "nand" ]; then
            val=`expr $(echo $(($nor_dpaa2_mc_dpl_offset))) / 1024`
            dd if=$DEPLOYDIR/$dpaa2_mc_dpl of=$fwimg bs=1K seek=$val
        elif [ "$2" = "sd" -o "$2" = "emmc" ]; then
            dd if=$DEPLOYDIR/$dpaa2_mc_dpl of=$fwimg bs=512 seek=$sd_dpaa2_mc_dpl_offset
        fi
    fi
    # DPAA2 DPC firmware
    if [ -n "$dpaa2_mc_dpc" ]; then
        if [ "$2" = "nor" -o "$2" = "qspi" -o "$2" = "xspi" -o "$2" = "nand" ]; then
            val=`expr $(echo $(($nor_dpaa2_mc_dpc_offset))) / 1024`
            dd if=$DEPLOYDIR/$dpaa2_mc_dpc of=$fwimg bs=1K seek=$val
        elif [ "$2" = "sd" -o "$2" = "emmc" ]; then
            dd if=$DEPLOYDIR/$dpaa2_mc_dpc of=$fwimg bs=512 seek=$sd_dpaa2_mc_dpc_offset
        fi
    fi

    # linux kernel image
    if [ "$2" = "qspi" ] && [ "$1" = "ls1021atwr" -o "$1" = "ls1043aqds" ]; then
        echo no itb in $1 composite firmware as $1 $2 flash no more than 16MiB
    elif [ "$2" = "nor" -o "$2" = "qspi" -o "$2" = "xspi" -o "$2" = "nand" ]; then
        val=`expr $(echo $(($nor_kernel_offset))) / 1024`
        dd if=$DEPLOYDIR/${kernel_itb} of=$fwimg bs=1K seek=$val
    elif [ "$2" = "sd" -o "$2" = "emmc" ]; then
        dd if=$DEPLOYDIR/${kernel_itb} of=$fwimg bs=512 seek=$sd_kernel_offset
    fi

    if [ "$2" = "sd" -o "$2" = "emmc" ]; then
        tail -c +4097 $fwimg > $fwimg.img && rm $fwimg
    else
        mv $fwimg $fwimg.img
    fi
    echo -e "${GREEN} $fwimg.img   [Done]\n${NC}"

}

generate_composite_fw_2M() {
    # generate machine-specific firmware to adapt to small footprint flash media, e.g. on LS1012A-FRWY
    # $1: machine name
    # $2: boot type: nor, sd, qspi, xspi, nand
    # $3: bootloader type: uboot or uefi
    # $4: optional argument, e.g. for 512mb

    echo "Generating $2boot composite firmware image for $1 ..."
    . $1.manifest
    . memorylayout.cfg
    if [ "$SECURE" = "true" ]; then
        bl2img=`eval echo '${'"atf_bl2_""$2"'_sec}'`
        fipimg=`eval echo '${'"atf_fip_""$3"'_sec}'`
        fwimg=$DEPLOYDIR/firmware_${1}_${3}_${2}boot_secure
        secureboot_headers=`eval echo '${'"secureboot_headers_""$2"'}'`
    else
        bl2img=`eval echo '${'"atf_bl2_""$2"'}'`
        fipimg=`eval echo '${'"atf_fip_""$3"'}'`
        fwimg=$DEPLOYDIR/firmware_${1}_${3}_${2}boot
    fi

    if [ "$4" = "512mb" ]; then
	if [ "$SECURE" = "true" ]; then
	    fwimg=$DEPLOYDIR/firmware_${1}_512mb_${3}_${2}boot_secure
	    bl2img=`eval echo '${'"atf_bl2_""$2_$4"'_sec}'`
	    fipimg=`eval echo '${'"atf_fip_""$3_$4"'_sec}'`
	else
	    fwimg=$DEPLOYDIR/firmware_${1}_512mb_${3}_${2}boot
	    bl2img=`eval echo '${'"atf_bl2_""$2_$4"'}'`
	    fipimg=`eval echo '${'"atf_fip_""$3_$4"'}'`
	fi
    fi

    [ -f $fwimg ] && rm -f $fwimg
    [ -z "$bl2img" ] && echo ${3} ${2}boot on $1 based on ATF: unsupported! && return

    # 1. program ATF bl2
    if [ "$2" = "sd" -o "$2" = "emmc" ]; then
        dd if=$DEPLOYDIR/$bl2img of=$fwimg bs=512 seek=$sd2_rcw_offset
    else
        dd if=$DEPLOYDIR/$bl2img of=$fwimg bs=1K seek=0
    fi

    # 2. reserved

    # 3. program Ethernet firmware,  e.g. PFE on LS1012A-FRWY
    if [ -n "$pfe_fw" ]; then
	if [ "$2" = "nor" -o "$2" = "qspi" -o "$2" = "xspi" -o "$2" = "nand" ]; then
	    val=`expr $(echo $(($nor2_eth_firmware_offset))) / 1024`
	    dd if=$DEPLOYDIR/$pfe_fw of=$fwimg bs=1K seek=$val
	elif [ "$2" = "sd" -o "$2" = "emmc" ]; then
	    dd if=$DEPLOYDIR/$pfe_fw of=$fwimg bs=512 seek=$sd2_eth_firmware_offset
	fi
    fi

    # 4. ATF FIP image
    if [ "$2" = "sd" -o "$2" = "emmc" ]; then
	dd if=$DEPLOYDIR/$fipimg of=$fwimg bs=512 seek=$sd2_fip_offset
    else
	val=`expr $(echo $(($nor2_fip_offset))) / 1024`
	dd if=$DEPLOYDIR/$fipimg of=$fwimg bs=1K seek=$val
    fi

    # 5. program bootloader environment varialbe
    if [ "$3" = "uefi" ]; then
	if [ -n "$uefi_env" ]; then
	    if [ "$2" = "nor" -o "$2" = "qspi" -o "$2" = "xspi" ]; then
		val=`expr $(echo $(($nor2_bootloader_env_offset))) / 1024`
		dd if=$DEPLOYDIR/$uefi_env of=$fwimg bs=1K seek=$val
	    elif [ "$2" = "sd" -o "$2" = "emmc" ]; then
		dd if=$DEPLOYDIR/$uefi_env of=$fwimg bs=512 seek=$sd2_bootloader_env_offset
	    fi
	fi
    fi

    # 6. flashing image script or reserved, 0x1E0000 64KB

    # 7. program secure boot headers
    if [ -n "$secureboot_headers" -a "$SECURE" = "true" ]; then
	if [ "$2" = "nor" -o "$2" = "qspi" -o "$2" = "nand" ]; then
	    val=`expr $(echo $(($nor2_secureboot_headers_offset))) / 1024`
	    dd if=$DEPLOYDIR/$secureboot_headers of=$fwimg bs=1K seek=$val
	elif [ "$2" = "sd" -o "$2" = "emmc" ]; then
	    dd if=$DEPLOYDIR/$secureboot_headers of=$fwimg bs=512 seek=$sd2_secureboot_headers_offset
	fi
    fi

    [ "$2" = "sd" -o "$2" = "emmc" ] &&	tail -c +4097 $fwimg > $fwimg.img && rm $fwimg || mv $fwimg $fwimg.img
    echo -e "${GREEN} $fwimg.img   [Done]\n${NC}"
}

if $SECURE; then
    secure_sign_image $MACHINE $BOOTTYPE
fi
if [ "$MACHINE" = "ls1012afrwy" ]; then
    generate_composite_fw_2M $MACHINE $BOOTTYPE uboot
    generate_composite_fw_2M $MACHINE $BOOTTYPE uboot 512mb
else
    generate_qoriq_composite_firmware $MACHINE $BOOTTYPE
fi
