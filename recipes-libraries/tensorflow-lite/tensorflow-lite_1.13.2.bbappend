RDEPENDS_${PN}_remove = "libnn-imx"
do_configure_prepend(){
        export HTTP_PROXY=${http_proxy}
        export HTTPS_PROXY=${https_proxy}
        export http_proxy=${http_proxy}
        export https_proxy=${https_proxy}
}
