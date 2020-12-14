#!/bin/bash

# BSD LICENSE
#
# Copyright 2017 NXP
#
#

Usage()
{
    echo "Usage: $0 -m MACHINE  -t BOOTTYPE -d TOPDIR -s DEPLOYDIR -e ENCAP -i IMA_EVM -o SECURE\

        -m        machine name
        -t        boottype
        -d        topdir
        -s        deploy dir
        -e        encap
        -i        ima-evm
        -o   secure
"
    exit
}

# get command options
while getopts "m:t:d:s:e:i:o:" flag
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
                e) ENCAP="$OPTARG";
                   echo "encap : $ENCAP";
                   ;;
                i) IMA_EVM="$OPTARG";
                   echo "ima_evm : $IMA_EVM";
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
    [ -z "$secureboot_headers" ] && echo ${2}boot secure on $1: unsupported && exit

    rm -f bootscript uImage.dtb uImage.bin kernel.itb secboot_hdrs*.bin hdr*.out
    cp $TOPDIR/$distro_bootscript $TOPDIR/bootscript && echo "Copying bootscript"
    cp $DEPLOYDIR/$device_tree $TOPDIR/uImage.dtb && echo "Copying dtb"
    cp $DEPLOYDIR/$kernel_img $TOPDIR/uImage.bin && echo "Copying kernel"
    cp $DEPLOYDIR/$kernel_itb $TOPDIR/kernel.itb && echo "Copying kernel_itb"

    rcwimg_nonsec=`eval echo '${'"rcw_""$2"'}'`
    if [ -z "$rcwimg_nonsec" ]; then
        echo ${2}boot on $1 is not supported && exit
    fi

    if [ "${1:0:7}" = "ls1021a" ]; then
        ubootimg_sec=`eval echo '${'"uboot_""$2"'boot_sec}'`
        if [ -z "$ubootimg_sec" ]; then
            echo $2 boot on $1 for secureboot unsupported
            exit
        fi
        if [ "$2" = "nor" -o "$2" = "qspi" ]; then
            cp $DEPLOYDIR/$ubootimg_sec $TOPDIR/u-boot-dtb.bin
        elif [ "$2" = "sd" -o "$2" = "emmc" ]; then
            if [ -z "$uboot_sdboot_sec" ]; then
                echo $2 boot on  for secureboot unsupported
                exit
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
    #cp $TOPDIR/$distro_bootscript $DEPLOYDIR/
    cp $TOPDIR/hdr_bs.out $DEPLOYDIR/secboot_hdrs/hdr_${1}_bs.out
    cp $TOPDIR/srk_hash.txt $DEPLOYDIR/
    cp $TOPDIR/srk.pri $DEPLOYDIR/
    cp $TOPDIR/srk.pub $DEPLOYDIR/
}


generate_distro_bootscr() {
    if [ "$ENCAP" = "true" ]; then
        KEY_ID=0x12345678123456781234567812345678
        key_id_1=${KEY_ID:2:8}
        key_id_2=${KEY_ID:10:8}
        key_id_3=${KEY_ID:18:8}
        key_id_4=${KEY_ID:26:8}
    fi
    . $1.manifest
    if [ -n "$distro_bootscript" ]; then
        if [ -n "$securevalidate" ]; then
            if [ "$ENCAP" = "true" ]; then
                if [ $bootscript_dec != null ]; then
                    echo $securevalidate_dec > $bootscript_dec.tmp
                    echo $distroboot >> $bootscript_dec.tmp
                    mkimage -A arm64 -O linux -T script -C none -a 0 -e 0  -n "boot.scr" \
			    -d $bootscript_dec.tmp $bootscript_dec
                    rm -f $bootscript_dec.tmp
                fi
                echo $securevalidate_enc > ${distro_bootscript}.tmp
            elif [ "$IMA_EVM" = "true" ] ; then
                if [ -n "$bootscript_enforce" ]; then
                    echo $securevalidate_enforce > $bootscript_enforce.tmp
                    echo $distroboot_ima >> $bootscript_enforce.tmp
                    mkimage -A arm64 -O linux -T script -C none -a 0 -e 0  -n "boot.scr" \
                             -d $bootscript_enforce.tmp $bootscript_enforce
                    rm -f $FBDIR/$bootscript_enforce.tmp
                fi
                echo $securevalidate_fix > ${distro_bootscript}.tmp
            else
                echo $securevalidate > ${distro_bootscript}.tmp
            fi
        fi
        if [ "$IMA_EVM" = "true" ] ; then
                echo $distroboot_ima >> ${distro_bootscript}.tmp
        else
                echo $distroboot >> ${distro_bootscript}.tmp
        fi

        mkimage -A arm64 -O linux -T script -C none -a 0 -e 0  -n "boot.scr" -d ${distro_bootscript}.tmp $distro_bootscript
        rm -f ${distro_bootscript}.tmp
        echo -e "${distro_bootscript}    [Done]\n"
    fi
}

generate_qoriq_composite_firmware() {
    # generate machine-specific firmware to be programmed to NOR/SD media
    # $1: machine name
    # $2: boot type: sd, qspi, xspi, nor, nand

    . $1.manifest
    . memorylayout.cfg
    if [ "$SECURE" = "true" ]; then
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
            # program rcw
            if [ -z "$rcwimg" ]; then echo ${2}boot on ${1} is not unsupported!; exit; fi
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

generate_distro_bootscr $MACHINE
secure_sign_image $MACHINE $BOOTTYPE
generate_qoriq_composite_firmware $MACHINE $BOOTTYPE
