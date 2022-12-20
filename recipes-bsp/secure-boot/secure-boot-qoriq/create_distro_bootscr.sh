#!/bin/bash
set -xe
# MIT License
# Copyright 2022 NXP

# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
# 
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

Usage()
{
    echo "Usage: $0 -m MACHINE -d TOPDIR -s DEPLOYDIR -e ENCAP -i IMA_EVM -o SECURE\

        -m        machine name
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
    cp $TOPDIR/$distro_bootscript $DEPLOYDIR/
}

generate_distro_bootscr $MACHINE
