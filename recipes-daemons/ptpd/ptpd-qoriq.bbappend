PROVIDES = "ptpd"

python() {
    pkgs = d.getVar('PACKAGES', True).split()
    for p in pkgs:
        if 'ptpd-qoriq' in p:
            d.appendVar("RPROVIDES_%s" % p, p.replace('ptpd-qoriq', 'ptpd'))
            d.appendVar("RCONFLICTS_%s" % p, p.replace('ptpd-qoriq', 'ptpd'))
            d.appendVar("RREPLACES_%s" % p, p.replace('ptpd-qoriq', 'ptpd'))
}

