# Layerscape Linux Yocto Project BSP 6.18.37_2.1.0 Release

Supplemental Yocto layer to hold pending patches to be upstreamed.

---

## Supported Boards

> Replace `<machine>` in all commands below with one of the board names listed here.

| Machine Name       |
|--------------------|
| ls1012ardb         |
| ls1012afrwy        |
| ls1021atwr         |
| ls1043ardb         |
| ls1046ardb         |
| ls1046afrwy        |
| ls1088ardb-pb      |
| ls1028ardb         |
| ls2088ardb         |
| lx2160ardb-rev2    |
| lx2162aqds         |

---

## Install the `repo` Utility

The `repo` tool must be installed before fetching the BSP manifest:

```bash
mkdir ~/bin
curl https://commondatastorage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
chmod a+x ~/bin/repo
export PATH=${PATH}:~/bin
```

---

## Install Essential Host Packages

Your build host must have the required Yocto packages installed.
See the [Yocto Project Quick Build - Host Packages](https://docs.yoctoproject.org/brief-yoctoprojectqs/index.html#build-host-packages) for details.

---

## Download Yocto Project BSP

```bash
mkdir yocto-sdk
cd yocto-sdk
repo init -u https://github.com/nxp-qoriq/yocto-sdk -b wrynose -m ls-6.18.37-2.1.0.xml
repo sync --force-sync
```

---

## Create Layerscape Environment

Source the setup script with your target machine name:

```bash
. ./setup-env -m lx2160ardb-rev2
```

If resuming an existing build folder:

```bash
cd build_lx2160ardb-rev2
source SOURCE_THIS
```

---

## Build Rootfs Image

```bash
bitbake fsl-image-networking
```

Or the full variant:

```bash
bitbake fsl-image-networking-full
```

**Output:** `tmp/deploy/images/<machine>/fsl-image-networking-<machine>.rootfs.tar.gz`

---

## Build Boot Image

```bash
bitbake qoriq-composite-firmware
```

**Output:** `tmp/deploy/images/<machine>/firmware_<machine>_uboot_sdboot.img`

---

## Generate Boot Partition Tarball

```bash
bitbake generate-boottgz
```

**Output:** `tmp/deploy/images/<machine>/boot_<machine>_lts_<kernel_version>.tgz`

---

## Deploy Images via flex-installer

Copy `flex-installer` to the Linux host PC:

```bash
sudo cp meta-qoriq/meta-qoriq-sdk/recipes-extended/flex-installer/flex-installer/flex-installer /usr/bin/
sudo chmod +x /usr/bin/flex-installer
```

Insert the SD card, then format it and deploy the BSP firmware, boot tarball and rootfs:

```bash
# Step 1: Format the SD card
flex-installer -i pf -d /dev/mmcblkX

# Step 2: Flash firmware, boot tarball and rootfs
flex-installer -m <machine> \
    -d /dev/<sdX or mmcblkX> \
    -f firmware_<machine>_uboot_sdboot.img \
    -b boot_<machine>_lts_<kernel_version>.tgz \
    -r fsl-image-networking-<machine>.rootfs.tar.gz
```
