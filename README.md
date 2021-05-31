# meta-rockchip

Yocto BSP layer for the Rockchip SOC boards
  - wiki <http://opensource.rock-chips.com/wiki_Main_Page>.

This README file contains information on building and booting the meta-rockchip BSP layers.

Please see the corresponding sections below for details.

## Dependencies

This layer depends on:

* URI: git://git.yoctoproject.org/poky
* branch: zeus dunfell gatesgarth hardknott

* URI: git://git.openembedded.org/meta-openembedded
* layers: meta-oe
* branch: zeus dunfell gatesgarth hardknott

* URI: git://git.openembedded.org/meta-python2
* branch: zeus dunfell gatesgarth hardknott

## Table of Contents

I. Configure yocto/oe Environment

II. Building meta-rockchip BSP Layers

III. Booting your Device

IV. Tested Hardwares

V. Supporting new Machine

### I. Configure yocto/oe Environment

In order to build an image with BSP support for a given release, you need to download the corresponding layers described in the "Dependencies" section. Be sure that everything is in the same directory.

```shell
~ $ mkdir yocto; cd yocto
~/yocto $ git clone git://git.yoctoproject.org/poky -b dunfell
~/yocto $ git clone git://git.openembedded.org/meta-openembedded.git -b dunfell
~/yocto $ git clone git://git.openembedded.org/meta-python2.git -b dunfell
```

And put the meta-rockchip layer here too.

Then you need to source the configuration script:

```shell
~/yocto $ source poky/oe-init-build-env
```

Having done that, you can build a image for a rockchip board by adding the location of the meta-rockchip layer to bblayers.conf, along with any other layers needed.

For example:

```makefile
# build/conf/bblayers.conf
BBLAYERS ?= " \
  ${TOPDIR}/../meta-rockchip \
  ${TOPDIR}/../poky/meta \
  ${TOPDIR}/../poky/meta-poky \
  ${TOPDIR}/../poky/meta-yocto-bsp \
  ${TOPDIR}/../meta-openembedded/meta-oe \
  ${TOPDIR}/../meta-python2 \
```

To enable a particular machine, you need to add a MACHINE line naming the BSP to the local.conf file:

```makefile
  MACHINE = "xxx"
```

All supported machines can be found in meta-rockchip/conf/machine.

### II. Building meta-rockchip BSP Layers

You should then be able to build a image as such:

```shell
$ bitbake core-image-minimal
```

At the end of a successful build, you should have an .wic image in /path/to/yocto/build/tmp/deploy/\<MACHINE\>/, also with an rockchip firmware image: update.img.

### III. Booting your Device

Under Linux, you can use upgrade_tool: <http://opensource.rock-chips.com/wiki_Upgradetool> to flash the image:

1. Put your device into rockusb mode: <http://opensource.rock-chips.com/wiki_Rockusb>

2. If it's maskrom rockusb mode, try to enter miniloader rockusb mode:

```shell
$ sudo upgrade_tool db \<IMAGE PATH\>/loader.bin
```

3. Flash the image (wic image or rockchip firmware image)

```shell
$ sudo upgrade_tool wl 0 \<IMAGE PATH\>/\<IMAGE NAME\>.wic # For wic image
```

```shell
$ sudo upgrade_tool uf \<IMAGE PATH\>/update.img # For rockchip firmware image
```

### IV. Tested Hardwares

The following undergo regular basic testing with their respective MACHINE types.

* px3se evb board

* rk3308 evb board

* rk3326 evb board

* px30 evb board

* rk3328 evb board

* rk3288 evb board

* rk3399 sapphire excavator board

* rk3399pro evb board

### V. Supporting new Machine

To support new machine, you can either add new machine config in meta-rockchip/conf/machine, or choose a similar existing machine and override it's configurations in local config file.

In general, a new machine needs to specify it's u-boot config, kernel config, kernel device tree and wifi/bt firmware:

For example:

```makefile
KBUILD_DEFCONFIG = "rk3326_linux_defconfig"
KERNEL_DEVICETREE = "rockchip/rk3326-evb-lp3-v10-linux.dtb"
UBOOT_MACHINE = "evb-rk3326_defconfig"
RK_WIFIBT_FIRMWARES = " \
        rkwifibt-firmware-ap6212a1-wifi \
        rkwifibt-firmware-ap6212a1-bt \
        brcm-tools \
"
```

If you want to use your own local u-boot and kernel sources, a simple way is to override related configurations in local config file.

For example using the kernel/ and u-boot/ in the same directory of meta-rockchip:

```makefile
# build/conf/local.conf
SRC_URI_pn-linux-rockchip = " \
        git://${TOPDIR}/../kernel;protocol=file;usehead=1 \
        file://cgroups.cfg \
"
SRCREV_pn-linux-rockchip = "${AUTOREV}"
KBRANCH = "HEAD"

SRC_URI_pn-u-boot = " \
        git://${TOPDIR}/../u-boot;protocol=file;usehead=1 \
"
SRCREV_pn-u-boot = "${AUTOREV}"
```

## Maintainers

* Jeffy Chen `<jeffy.chen@rock-chips.com>`
