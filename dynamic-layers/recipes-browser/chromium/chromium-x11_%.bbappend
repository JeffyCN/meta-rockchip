# Copyright (C) 2021, Rockchip Electronics Co., Ltd
# Released under the MIT license (see COPYING.MIT for the terms)

DEPENDS:append = " libxshmfence libxkbcommon"

# redefining do_copy_target_rustlibs to fix duplicates from rust-cross vs 
# libstd-rs causing symbol hash mismatches at link time.
do_copy_target_rustlibs () {
    # Chromium needs a single Rust sysroot that contains the rustlibs for both
    # the host and target, so we copy the target rustlibs to the native sysroot.
    # Remove old rlibs/rmeta first to avoid duplicates from rust-cross vs
    # libstd-rs causing symbol hash mismatches at link time.
    # Use RUST_TARGET_SYS (not TARGET_ARCH*) to avoid nuking host rlibs.
    rustlib_src_dir="${STAGING_LIBDIR}/rustlib/${TARGET_ARCH}"*
    if [ -d "${STAGING_LIBDIR_NATIVE}/rustlib/${RUST_TARGET_SYS}/lib" ]; then
        rm -f "${STAGING_LIBDIR_NATIVE}/rustlib/${RUST_TARGET_SYS}/lib"/*.rlib
        rm -f "${STAGING_LIBDIR_NATIVE}/rustlib/${RUST_TARGET_SYS}/lib"/*.rmeta
    fi
    cp -r $rustlib_src_dir "${STAGING_LIBDIR_NATIVE}/rustlib"
}