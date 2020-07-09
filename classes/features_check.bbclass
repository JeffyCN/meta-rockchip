# Copyright (C) 2020, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)
#
# This bbclass is extremely hacky.

inherit ${@oe.utils.ifelse(os.path.exists("../poky/meta/classes/features_check.bbclass"), 'features_check-core', 'distro_features_check')}
