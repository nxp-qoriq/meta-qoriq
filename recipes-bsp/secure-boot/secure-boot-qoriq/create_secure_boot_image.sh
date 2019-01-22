#!/bin/bash

# BSD LICENSE
#
# Copyright 2017 NXP
#
#

Usage()
{
    echo "Usage: $0 -m MACHINE  -t BOOTTYPE -d TOPDIR -s DEPLOYDIR -e ENCAP -i IMA_EVM\

        -m        machine name
        -t        boottype
        -d        topdir
        -s        deploy dir
        -e        encap
        -i        ima-evm
"
    exit
}

# get command options
while getopts "m:t:d:s:e:i:" flag
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
                ?) Usage;
                   exit 3
                   ;;
        esac
done

secure_sign_image() {
    
    echo "Signing $2boot images for $1 ..."
    if [ "$ENCAP" = "true" ]; then
        cp $TOPDIR/$bootscript_dec $TOPDIR/bootscript_dec && echo "Copying bootscript_decap"
    fi

    cp $TOPDIR/$uboot_scr $TOPDIR/bootscript && echo "Copying bootscript"
    cp $DEPLOYDIR/$device_tree $TOPDIR/uImage.dtb && echo "Copying dtb"
    cp $DEPLOYDIR/$kernel_itb $TOPDIR/kernel.itb && echo "Copying kernel_itb"

    if [ $MACHINE = ls1021atwr ]; then
        cp $DEPLOYDIR/$kernel_uimg $TOPDIR/uImage.bin && echo "Copying kernel"
    else
        cp $DEPLOYDIR/$kernel_img $TOPDIR/uImage.bin && echo "Copying kernel"
    fi
    rcwimg_sec=`eval echo '${'"rcw_""$BOOTTYPE"'_sec}'`
    rcwimg_nonsec=`eval echo '${'"rcw_""$BOOTTYPE"'}'`
    if [ -f $DEPLOYDIR/$pfe_fw ] ; then
        cp $DEPLOYDIR/$pfe_fw $TOPDIR/pfe.itb && echo "Copying PFE"
    fi
     
    if [ -f $DEPLOYDIR/$dpaa2_mc_fw ] ; then
        cp $DEPLOYDIR/$dpaa2_mc_fw $TOPDIR/mc.itb
    fi

    if [ -f $DEPLOYDIR/$dpaa2_mc_dpc ] ; then
        cp $DEPLOYDIR/$dpaa2_mc_dpc $TOPDIR/dpc.dtb
    fi

    if [ -f $DEPLOYDIR/$dpaa2_mc_dpl ] ; then
        cp $DEPLOYDIR/$dpaa2_mc_dpl $TOPDIR/dpl.dtb
    fi

    if [ ! -d  $DEPLOYDIR/secboot_hdrs/ ] ; then
        mkdir -p  $DEPLOYDIR/secboot_hdrs/
    fi
    if [ $BOOTTYPE = nand ] ; then
        . $nand_script
    elif [ $BOOTTYPE = sd ] ; then
        . $sd_script
    elif [ $BOOTTYPE = nor ] ; then
        . $nor_script
    elif [ $BOOTTYPE = qspi ] ; then
        . $qspi_script
    elif [ $BOOTTYPE = xspi ] ; then
        . $xspi_script
    fi


    cp $TOPDIR/secboot_hdrs_${BOOTTYPE}boot.bin $DEPLOYDIR/secboot_hdrs/
    cp $TOPDIR/hdr_dtb.out $DEPLOYDIR/secboot_hdrs/
    cp $TOPDIR/hdr_linux.out $DEPLOYDIR/secboot_hdrs/
    if [  $MACHINE  = ls1012afrwy ] ; then
        cp $TOPDIR/hdr_kernel.out $DEPLOYDIR/secboot_hdrs/
    fi
    cp $TOPDIR/hdr_bs.out $DEPLOYDIR/secboot_hdrs/hdr_${1}_bs.out
    cp $TOPDIR/srk_hash.txt $DEPLOYDIR/
    cp $TOPDIR/srk.pri $DEPLOYDIR/
    cp $TOPDIR/srk.pub $DEPLOYDIR/
    if [ "$ENCAP" = "true" ]; then
        cp $TOPDIR/hdr_bs_dec.out $DEPLOYDIR/secboot_hdrs/hdr_${1}_bs_dec.out
    fi
}


generate_distro_bootscr() {
    if [ "$ENCAP" = "true" ] ; then
        KEY_ID=0x12345678123456781234567812345678
        key_id_1=${KEY_ID:2:8}
        key_id_2=${KEY_ID:10:8}
        key_id_3=${KEY_ID:18:8}
        key_id_4=${KEY_ID:26:8}
    fi
    . $MACHINE.manifest
    if [ -n "$uboot_scr" -a "$uboot_scr" != "null" ] ; then
        if [ -n "$securevalidate" ]; then
            if [ "$ENCAP" = "true" ] ; then
                if [ $bootscript_dec != null ] ; then
                    echo $securevalidate_dec > $bootscript_dec.tmp
                    if [ $MACHINE = ls1043ardb -o $MACHINE = ls1046ardb ]; then
                        echo $distroboot | sed 's/vmlinuz/vmlinuz.v8/g' >> $bootscript_dec.tmp
                    else
                        echo $distroboot >> $bootscript_dec.tmp
                    fi
                mkimage -A arm64 -O linux -T script -C none -a 0 -e 0  -n "boot.scr" -d $bootscript_dec.tmp $bootscript_dec
                rm -f $bootscript_dec.tmp
                fi
                echo $securevalidate_enc > $uboot_scr.tmp
            elif [ "$IMA_EVM" = "true" ] ; then
                 if [ $bootscript_enforce != null ] ; then
                     echo $securevalidate_enforce > $bootscript_enforce.tmp
                     echo $distroboot_ima >> $bootscript_enforce.tmp
                     mkimage -A arm64 -O linux -T script -C none -a 0 -e 0  -n "boot.scr" \
                             -d $bootscript_enforce.tmp $bootscript_enforce
                     rm -f $FBDIR/$bootscript_enforce.tmp
                 fi
                 echo $securevalidate_fix > $uboot_scr.tmp
            else
                echo $securevalidate > $uboot_scr.tmp
            fi
        fi
        if [ "$IMA_EVM" = "true" ] ; then
                echo $distroboot_ima >> $uboot_scr.tmp
        else
                echo $distroboot >> $uboot_scr.tmp
        fi

        mkimage -A arm64 -O linux -T script -C none -a 0 -e 0  -n "boot.scr" -d $uboot_scr.tmp $uboot_scr
        rm -f $uboot_scr.tmp
        echo -e "$uboot_scr    [Done]\n"
    fi
}
generate_distro_bootscr $MACHINE
secure_sign_image $MACHINE $BOOTTYPE
