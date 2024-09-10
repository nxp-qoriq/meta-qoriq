PACKAGES_DYNAMIC = "^vpp-plugin-.*"

PACKAGES =+ "${PN}-plugins ${PN}-data ${PN}-plugins-data"

python populate_packages:prepend() {
    plugindir = d.expand('${libdir}/vpp_plugins/')
    packages = " ".join(do_split_packages(d, plugindir, r'^(.*)_plugin\.so$', 'vpp-plugin-%s', 'vpp plugin for %s', extra_depends=''))
    plugindir = d.expand('${libdir}/vpp_api_test_plugins/')
    packages += " " + " ".join(do_split_packages(d, plugindir, r'^(.*)_plugin\.so$', 'vpp-plugin-%s', 'vpp plugin for %s', extra_depends=''))
    plugindir = d.expand('${libdir}/vat2_plugins/')
    packages += " " + " ".join(do_split_packages(d, plugindir, r'^(.*)_plugin\.so$', 'vpp-plugin-%s', 'vpp plugin for %s', extra_depends=''))
    d.setVar("RDEPENDS:vpp-plugins", packages)
}

FILES:${PN} += " \
		${prefix}${sysconfdir} \
		${sysconfdir}/vpp \
		${sysconfdir}/rc.local \
		"

FILES:${PN}-dev += " \
                ${libdir}/cmake/vpp/*.cmake \
		"

FILES:${PN}-data = " \
		${datadir}/vpp/api/core/*.json \
		${datadir}/vpp/C.py \
		${datadir}/vpp/JSON.py  \
                ${datadir}/vpp/vppapigen_json.py \
                ${datadir}/vpp/vppapigen_c.py \
		"


FILES:${PN}-plugins-data = " \
		${datadir}/vpp/api/plugins/*.json \
                ${datadir}/vpp/plugins/perfmon/PerfmonTables.tar.xz \
		"

ALLOW_EMPTY:${PN}-plugins = "1"

