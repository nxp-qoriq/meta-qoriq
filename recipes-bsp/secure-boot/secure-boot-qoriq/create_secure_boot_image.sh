#!/bin/bash

# BSD LICENSE
#
# Copyright 2017 NXP
#
#

Usage()
{
    echo "Usage: $0 -m MACHINE  -t BOOTTYPE -d TOPDIR -s DEPLOYDIR -e ENCAP\

        -m        machine name
        -t        boottype
        -d        topdir
        -s        deploy dir
        -e        encap
"
    exit
}

# get command options
while getopts "m:t:d:s:e:" flag
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
                ?) Usage;
                   exit 3
                   ;;
        esac
done

secure_sign_image() {
    
    echo "Signing $2boot images for $1 ..."
    if [ ! -f $DEPLOYDIR/$kernel_uimg ]; then
        echo $DEPLOYDIR/$kernel_uimg unpresent, generate it ...
        cp $DEPLOYDIR/$kernel_img $TOPDIR/
        rm -rf $kernel_img.gz 
        gzip $kernel_img 
        mkimage -A arm64 -O linux -T kernel -C gzip  -a 0x80080000 \
                -e 0x80080000 -n Linux -d $kernel_img.gz $kernel_uimg
        cp $TOPDIR/$kernel_uimg $DEPLOYDIR
    fi 
    if [ "$ENCAP" = "y" ]; then
        cp $TOPDIR/$bootscript_dec $TOPDIR/bootscript_dec && echo "Copying bootscript_decap"
    fi
    if [ -f ${DEPLOYDIR}/fsl-ls1043a-rdb-sdk.dtb ]; then
        mv ${DEPLOYDIR}/fsl-ls1043a-rdb-sdk.dtb ${DEPLOYDIR}/fsl-ls1043a-rdb.dtb
    fi
    if [ -f ${DEPLOYDIR}/fsl-ls1046a-rdb-sdk.dtb ]; then
        mv ${DEPLOYDIR}/fsl-ls1046a-rdb-sdk.dtb ${DEPLOYDIR}/fsl-ls1046a-rdb.dtb
    fi

    cp $TOPDIR/$uboot_scr $TOPDIR/bootscript && echo "Copying bootscript"
    cp $DEPLOYDIR/$device_tree $TOPDIR/uImage.dtb && echo "Copying dtb"
    cp $DEPLOYDIR/$kernel_itb $TOPDIR/kernel.itb && echo "Copying kernel_itb"

    if [ $MACHINE = ls1021atwr ]; then
        cp $DEPLOYDIR/$kernel_uimg $TOPDIR/uImage.bin && echo "Copying kernel"
    else
        cp $DEPLOYDIR/$kernel_uimg $TOPDIR/uImage.bin && echo "Copying kernel"
    fi
    rcwimg_sec=`eval echo '${'"rcw_""$BOOTTYPE"'_sec}'`
    rcwimg_nonsec=`eval echo '${'"rcw_""$BOOTTYPE"'}'`
    if [ $BOOTTYPE = nor -o $BOOTTYPE = qspi ] ; then
        if [ -z "$rcwimg_sec" -o "$rcwimg_sec" = "null" ]; then
            echo ${BOOTTYPE}boot on $1 for secureboot unsupported!
            exit
        elif [ ! -f $DEPLOYDIR/$rcwimg_sec ]; then
            echo $DEPLOYDIR/$rcwimg_sec unpresent, building it...
        fi
        if [ $MACHINE = ls2088ardb -o $MACHINE = ls1088ardb ] ; then
            if [ -z "$rcwimg_nonsec" -o "$rcwimg_nonsec" = "null" ]; then
                echo ${BOOTTYPE}boot on $1 not unsupported!
                exit
            fi
            cp $DEPLOYDIR/$rcwimg_nonsec $TOPDIR/rcw.bin
        fi
        ubootimg_sec=`eval echo '${'"uboot_""$BOOTTYPE"'boot_sec}'`
        if [ -z "$ubootimg_sec" -o "$ubootimg_sec" = "null" ]; then
            echo ${BOOTTYPE}boot on $MACHINE for secureboot unsupported
            exit
        fi
        cp $DEPLOYDIR/$ubootimg_sec $TOPDIR/u-boot-dtb.bin
    elif [ $BOOTTYPE = sd ] ; then
          if [ $MACHINE = ls1088ardb -o $MACHINE = ls2088ardb ] ; then
            if [ -z "$rcwimg_nonsec" -o "$rcwimg_nonsec" = "null" ]; then
                echo ${BOOTTYPE}boot on $MACHINE not unsupported!
                exit
            fi
            cp $DEPLOYDIR/$rcwimg_nonsec $TOPDIR/rcw.bin
          fi
          if [ "$uboot_sdboot_sec" = "null" -o -z "$uboot_sdboot_sec" ]; then
              echo ${BOOTTYPE}boot on $MACHINE for secureboot unsupported
              exit
          fi
          cp $DEPLOYDIR/$uboot_sdboot_sec $TOPDIR/u-boot-with-spl-pbl.bin
          cp $DEPLOYDIR/$uboot_spl $TOPDIR/u-boot-spl.bin
          cp $DEPLOYDIR/$uboot_dtb $TOPDIR/u-boot-dtb.bin
    fi

    if [ -f $DEPLOYDIR/$ppa ] ; then
        cp $DEPLOYDIR/$ppa $TOPDIR/ && echo "Copying PPA"
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
    fi

    if [ $BOOTTYPE = sd ] ; then
        if [  $MACHINE = ls2088ardb -o $MACHINE = ls1088ardb ] ; then
            cp $TOPDIR/rcw_sec.bin $TOPDIR/$uboot_sdboot_sec
        else
            cp $TOPDIR/u-boot-with-spl-pbl-sec.bin $TOPDIR/$uboot_sdboot_sec
        fi
    elif [ $BOOTTYPE = nand ]; then
        cp $TOPDIR/u-boot-with-spl-pbl-sec.bin $TOPDIR/$uboot_nandboot_sec
    else
        if [ $MACHINE = ls2088ardb -o $MACHINE = ls1088ardb ] && [ -f $TOPDIR/rcw_sec.bin ]; then
            cp $TOPDIR/rcw_sec.bin $DEPLOYDIR/$rcwimg_sec
        fi
    fi

    cp $TOPDIR/secboot_hdrs.bin $DEPLOYDIR/secboot_hdrs/
    cp $TOPDIR/hdr_dtb.out $DEPLOYDIR/secboot_hdrs/
    cp $TOPDIR/hdr_linux.out $DEPLOYDIR/secboot_hdrs/
    cp $TOPDIR/hdr_bs.out $DEPLOYDIR/secboot_hdrs/hdr_${1}_bs.out
    cp $TOPDIR/srk_hash.txt $DEPLOYDIR/
    cp $TOPDIR/srk.pri $DEPLOYDIR/
    cp $TOPDIR/srk.pub $DEPLOYDIR/
    if [ "$ENCAP" = "y" ]; then
        cp $TOPDIR/hdr_bs_dec.out $DEPLOYDIR/secboot_hdrs/hdr_${1}_bs_dec.out
    fi
}


generate_distro_bootscr() {
    . $MACHINE.manifest
    if [ -n "$uboot_scr" -a "$uboot_scr" != "null" ] ; then
        if [ -n "$securevalidate" ]; then
            if [ "$ENCAP" = "y" ] ; then
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
            else
                echo $securevalidate > $uboot_scr.tmp
            fi
        fi
        if [ $MACHINE = ls1043ardb -o $MACHINE = ls1046ardb ]; then
            echo $distroboot | sed 's/vmlinuz/vmlinuz.v8/g' >> $uboot_scr.tmp
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
