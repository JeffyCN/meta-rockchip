# meta-rockchip

Yocto BSP layer for the Rockchip SOC boards  
  - wiki <http://opensource.rock-chips.com/wiki_Main_Page>.

This README file contains information on building and booting the
meta-rockchip BSP layers.  
Please see the corresponding sections below for details.

## Dependencies

This layer depends on:

* URI: git://git.yoctoproject.org/poky
* branch: warrior zeus dunfell

* URI: git://git.openembedded.org/meta-openembedded
* layers: meta-oe
* branch: warrior zeus dunfell

* URI: git://git.openembedded.org/meta-python2
* branch: warrior zeus dunfell

## Table of Contents

  I. Configure yocto/oe environment  
 II. Building meta-rockchip BSP layers  
III. Booting your device  
 IV. Tested Hardware  

### I. Configure yocto/oe environment

In order to build an image with BSP support for a given release, you
need to download the corresponding tools described in the "Dependencies"
section. Be sure that everything is in the same directory.

~ $ mkdir yocto; cd yocto  
~/yocto $ git clone git://git.yoctoproject.org/poky -b zeus
~/yocto $ git clone git://git.openembedded.org/meta-openembedded.git -b zeus
~/yocto $ git clone git://git.openembedded.org/meta-python2.git -b zeus

And put the meta-rockchip layer here too.

Then you need to source the configuration script:
~/yocto $ source poky/oe-init-build-env

Having done that, you can build a image for a rockchip board by adding
the location of the meta-rockchip layer to bblayers.conf, along with any
other layers needed e.g.:

  ${TOPDIR}/../meta-rockchip \
  ${TOPDIR}/../poky/meta \
  ${TOPDIR}/../poky/meta-poky \
  ${TOPDIR}/../poky/meta-yocto-bsp \
  ${TOPDIR}/../meta-openembedded/meta-oe \
  ${TOPDIR}/../meta-python2 \

To enable a particular machine, you need to add a MACHINE line naming
the BSP to the local.conf file:

  MACHINE ?= "xxx"

All supported machines can be found in meta-rockchip/conf/machine.

### II. Building meta-rockchip BSP layers

You should then be able to build a image as such:

  $ bitbake core-image-minimal

At the end of a successful build, you should have an .wic image in  
/path/to/yocto/build/tmp/deploy/\<MACHINE\>/, also with an rockchip  
firmware image: update.img.

### III. Booting your device

Under Linux, you can use these tools to flash the image:

* upgrade_tool: <http://opensource.rock-chips.com/wiki_Upgradetool>
* rkdeveloptool: <http://opensource.rock-chips.com/wiki_Rkdeveloptool>

Assuming you are using upgrade_tool:

1. Put your device into rockusb mode:  
    <http://opensource.rock-chips.com/wiki_Rockusb>
2. If it's maskrom rockusb mode, try to enter miniloader rockusb mode:  
     $ sudo upgrade_tool db \<IMAGE PATH\>/loader.bin
3. Flash the image (wic image or rockchip firmware image)  
     For wic image:  
       $ sudo upgrade_tool wl 0 \<IMAGE PATH\>/\<IMAGE NAME\>.wic

     For rockchip firmware image:  
       $ sudo upgrade_tool uf \<IMAGE PATH\>/update.img  

### IV. Tested Hardware

The following undergo regular basic testing with their respective MACHINE types.

* px3se evb board
* rk3308 evb board
* rk3326 evb board
* rk3288 evb board
* rk3399 sapphire excavator board

## Maintainers

* Jeffy Chen `<jeffy.chen@rock-chips.com>`
