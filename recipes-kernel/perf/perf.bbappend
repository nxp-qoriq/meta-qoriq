do_configure:prepend:qoriq () {
    # use /usr/bin/env instead of the fixed path of sh
    if [ -e ${S}/tools/perf/scripts/python/bin/flamegraph-record ]; then
        sed -i 's,/usr/bin/sh,/usr/bin/env sh,' "${S}/tools/perf/scripts/python/bin/flamegraph-record"
    fi
    # use /usr/bin/env instead of the fixed path of sh
    if [ -e ${S}/tools/perf/scripts/python/bin/flamegraph-report ]; then
        sed -i 's,/usr/bin/sh,/usr/bin/env sh,' "${S}/tools/perf/scripts/python/bin/flamegraph-report"
    fi
}
