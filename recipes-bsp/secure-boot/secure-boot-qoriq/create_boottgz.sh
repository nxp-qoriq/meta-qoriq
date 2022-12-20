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
    echo "Usage: $0 -m MACHINE  -s DEPLOYDIR -v kernel_version\

        -m        machine name
        -s        deploy dir
        -v        kernel version
"
    exit
}

# get command options
while getopts "m:s:v:" flag
do
    case $flag in
            m) MACHINE="$OPTARG";
                echo "machine: $MACHINE";
                ;;
            s) DEPLOYDIR="$OPTARG";
                echo "DEPLOYDIR : $DEPLOYDIR";
                ;;
            v) VERSION="$OPTARG";
                echo "KERNEL VERIOSN : $VERSION";
                ;;
            ?) Usage;
                exit 3
                ;;
    esac
done
generate_boottgz(){
    . $MACHINE.manifest

    find $img_dir/*.dtb -type l | xargs -i cp {} $boot_dir

    find $img_dir/module* -type l | xargs -i tar -xvf {} -C $boot_dir
    mv $boot_dir/lib/* $boot_dir/
    rm -rf $boot_dir/lib

    find $img_dir/$kernel_img -type l | xargs -i cp {} $boot_dir/
    gzip -kc $boot_dir/$kernel_img > $boot_dir/$kernel_img.gz

    cp $img_dir/${distro_bootscript} $boot_dir
    if [ -d $img_dir/secboot_hdrs/ ]; then
        mv $img_dir/secboot_hdrs $boot_dir
    fi
    if [-f $img_dir/srk* ]; then
        mv $img_dir/srk* $boot_dir
    fi
    tar -czvf $tarball.tgz -C $boot_dir/ .

}

#-----------------MainEntry--------------------------
if [ -z "$VERSION" ];then
    VERSION="6.1"
fi

img_dir=${DEPLOYDIR}
boot_dir="boot_${MACHINE}_lts_${VERSION}"
tarball=$img_dir/$boot_dir
rm -rf $boot_dir
mkdir -p $boot_dir

generate_boottgz
