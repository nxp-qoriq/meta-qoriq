Layerscape Linux Yocto Project BSP 6.12.20_2.0.0 Release
=====================================================
Supplemental Yocto layer to hold pending patches to be upstreamed.


Supported boards:
--------------------------
  * ls1012ardb
  * ls1012afrwy
  * ls1021atwr
  * ls1043ardb
  * ls1046ardb
  * ls1046afrwy
  * ls1088ardb-pb
  * ls1028ardb
  * ls2088ardb
  * lx2160ardb-rev2
  * lx2162aqds


Install the `repo` utility
--------------------------
To use this manifest repo, the 'repo' tool must be installed first
```
$ mkdir ~/bin
$ curl http://commondatastorage.googleapis.com/git-repo-downloads/repo  > ~/bin/repo
$ chmod a+x ~/bin/repo
$ PATH=${PATH}:~/bin
```


Install essential host packages
------------------------------
Your Build Host must install required packages for the Yocto build.
Reference to the section *Build Host Packages* in the document ***Yocto Project Quick build***.
- https://docs.yoctoproject.org/brief-yoctoprojectqs/index.html#build-host-packages


Download Yocto project BSP
------------------------------
```
$ mkdir yocto-sdk
$ cd yocto-sdk
$ repo init -u https://github.com/nxp-qoriq/yocto-sdk -b walnascar -m ls-6.12.20-2.0.0.xml
$ repo sync --force-sync
```


Create Layerscape environment
------------------------------
```
$ . ./setup-env -m lx2160ardb-rev2
```
If Use an existing build folder
```
$ cd build_lx2160ardb-rev2
$ source SOURCE_THIS
```


Build rootfs image
------------------------------
```
$ bitbake fsl-image-networking
```
or:
```
$ bitbake fsl-image-networking-full
```
fsl-image-networking-<machine>.rootfs.tar.gz will be found under tmp/deploy/images/lx2160ardb-rev2/


Build boot image
------------------------------
```
$ bitbake qoriq-composite-firmware
```
firmware_<machine>_uboot_sdboot.img will be found under tmp/deploy/images/lx2160ardb-rev2/


Generate boot partition tarball
------------------------------
```
$ bitbake generate-boottgz
```
boot_<machine>_lts_6.12.tgz will be found under tmp/deploy/images/lx2160ardb-rev2/


Install image by flex-installer
------------------------------
prepare for install flex-installer on Linux host PC
```
$ sudo cp meta-qoriq/meta-qoriq-sdk/recipes-extended/flex-installer/flex-installer/flex-installer  /user/bin/
$ sudo chmod +x /usr/bin/flex-installer
```
 Plugin SD card on linux host PC and install Layerscape BSP firmware, boot tarball and Yocto-rootfs as below:
```
$ flex-installer -i pf -d /dev/mmcblkX (format SD card)
$ flex-installer -m <machine> -d /dev/<sdX/mmcblkX> -f firmware_<machine>_uboot_sdboot.img -b boot_lts_6.12.tgz -r fsl-image-networking-<machine>.rootfs.tar.gz
```
