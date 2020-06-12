#!/bin/sh

# Copyright 2020 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

if [ -e /proc/cmdline ]; then
    updated=''
    
    # get current consoles from /etc/inittab
    current_console=`grep '/bin/start_getty' /etc/inittab |awk '{print $3}'`
    for console in $current_console;do
        if [ ! -c /dev/$console ];then
            sed -i -e /^.*${console}\ /d -e /^.*${console}$/d /etc/inittab
            updated=1
        fi
    done
    
    # get console from /proc/cmdline
    i=`sed -e 's/.*\(console=\S*\)\s*.*/\1/' /proc/cmdline`
    j=`echo $i | sed -e 's/^.*=//g' -e 's/,.*//g'`
    l=`echo $i | sed -e 's/^.*=//g' -e 's/,.*//g' -e 's/tty//'`
    if ! grep -q "/bin/start_getty.*$j" /etc/inittab && [ -c /dev/$j ];then
        echo "$l:12345:respawn:/bin/start_getty 115200,38400,9600 $j vt102" >> /etc/inittab
        updated=1
    fi
    
    if test $updated;then
        kill -HUP 1
    fi
fi

