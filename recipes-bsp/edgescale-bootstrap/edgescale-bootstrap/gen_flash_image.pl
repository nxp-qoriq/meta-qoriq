#!/usr/bin/env perl
eval 'LANG=C exec perl -w -S $0 ${1+"$@"}'
    if $running_under_some_shell;
$running_under_some_shell = 0;
$ENV{LANG} = 'C';

use strict;
use Getopt::Std;
use IO::Handle;
use String::CRC32;
use vars qw($opt_c $opt_d $opt_e $opt_o $opt_t $opt_h $out $fmap $ipath
            $opt_v @mtable $blocks $flash_start $flash_size
            $opt_p $env_size $env_endian $uboot_envfile);

$opt_c = "";
$opt_e = "";
$opt_d = "";
$opt_o = "";
$opt_t = "";
$opt_v = "";
$opt_h = "";
$opt_p = "";
@mtable = ();
$blocks = 0;
$flash_start = "";
$flash_size = "";
$env_size = "";
$env_endian = "";
$uboot_envfile = "environment.bin";

my $usage = <<TXT;

gen_flash_image.pl [-vph] [-c <var1>] [-e <var2>] [-d <var3>] [-o <var4>] [-t <var5>]
    where:
        -c <var1>     : flashmap config, 'flashmap.cfg' if not set
        -e <var2>     : uboot environment, binary will not generated if not set
        -d <var3>     : image location, current path if not set
        -o <var4>     : output file, 'flash.img' if not set
        -t <var5>     : tag string, no tag will be inserted if not set
        -p            : preserve tmp files
        -v            : verbose
        -h            : help

TXT

getopts("c:d:e:o:t:vph") or die $usage;
die $usage if $opt_h;
$fmap = ($opt_c eq '') ? "flashmap.cfg":$opt_c;
$out = ($opt_o eq '') ? "flash.img":$opt_o;
$ipath = ($opt_d eq '') ? ".":$opt_d;
$ipath =~ s/\/$//;
print "Generating flash image...\n";
print "  uboot env: $opt_e\n" if $opt_e ne '';
print "  flash map: $fmap\n  image path: $ipath\n";
print "  Output: $out\n";
if ($opt_t ne '') {
	printf "    Tag: $opt_t\n";
}

my $uboot_withtag = "u-boot_withtag.bin";
my $tmp = "";
if ($opt_e ne '') {
    $env_size = `grep env_size $fmap | cut -d ' ' -f2`;
    if ( $? != 0 ) {
        print "ERROR: grep command failed\n"; exit;
    }
    chomp($env_size);
    print "Uboot env size: $env_size\n" if $opt_v;
    gen_env($opt_e, $uboot_envfile, hex($env_size));
}
$env_endian = `grep env_endian $fmap | cut -d ' ' -f2`;
if ( $? != 0 ) {
    print "ERROR: env_endian not set\n"; exit;
}
chomp($env_endian);
die "env_endian is empty.\n" if ($env_endian eq '');
print "Uboot env endian: $env_endian\n" if $opt_v;

parse_cfg($fmap, $ipath);
$blocks = $#mtable;
cacul_space();

print "\nStart writing image..\n" if $opt_v;
open(my $IMAGE,">", $out) || die "Can not open $out: $! \n";;
binmode $IMAGE;
for (my $n = 0; $n <= $blocks; $n++) {
	open INFILE, $mtable[$n][1] or
		die "Can not open img $mtable[$n][1]! $! \n";
	binmode INFILE;
    # fill blank at the beginning, size: $mtable[$n][3]
	print "Block: $n filling front blank, " if $opt_v;
    fill_blank($IMAGE, $mtable[$n][3]);
	if (($mtable[$n][1] =~ /u-boot.*\.bin/) && ($opt_t ne '')) {
		close INFILE;
        # add uboot tag, write to file $uboot_withtag
		$tmp = add_uboottag($opt_t, $mtable[$n][2], $mtable[$n][1], $uboot_withtag) + $mtable[$n][0];
		printf "uboot tag position: 0x%08x\n", $tmp;
		open INFILE, $uboot_withtag or
	        die "Can not open img $uboot_withtag! $! \n";
	    binmode INFILE;
	}
    while(read(INFILE, $tmp, 4096)) {
        syswrite($IMAGE, $tmp, length($tmp));
    }
    # fill blank at the end, size: $mtable[$i][4]
	if (($mtable[$n][1] =~ /uImage.*\.bin/) && ($opt_t ne '')) {
		print "Adding tag $opt_t for $mtable[$n][1]\n" if $opt_v;
        printf "uImage tag position: 0x%08x\n", $mtable[$n][0] + $mtable[$n][2];
	    print "Block: $n filling end blank, " if $opt_v;
		fill_blank($IMAGE, $mtable[$n][4], $opt_t);
	}
	else {
	    print "Block: $n filling end blank, " if $opt_v;
		fill_blank($IMAGE, $mtable[$n][4]);
	}
	close INFILE;
}
close $IMAGE;
if ( ! $opt_p ) {
    print "\nDeleting tmp files...\n" if $opt_v;
    unlink $uboot_withtag;
    unlink $uboot_envfile;
}
print "\nFlash image generated.\n";

###########################################################
# Name: gen_env
# Description: generate uboot environment file
###########################################################
sub gen_env {
    my ($if, $of, $size) = @_;
    my $buffer = "";
    my $line = "";
    my $i;
    my $crc_str = "";
    my $crc_buffer = "";
    my $env_count = 0;
    $size -= 4;
    print "\nGenerating uboot env $of using $if\n" if $opt_v;
    open INFILE, $if or
        die "Can not open $if: $! \n";
    while (chomp($line = <INFILE>)) {
        for ($i=0; $i<length($line); $i++) {
            $buffer .= pack("A", substr($line , $i , 1));
            $env_count += 1;
        }
        $buffer .= pack("H2", "00");
        $env_count += 1;
    }
    close INFILE;
    die "uboot env setting too long, exceed max env size\n"
        if ($env_count >= $size);
	for ($i = $env_count; $i < $size; $i++) {
		$buffer .= pack("H2", "00");
	}
    $crc_str = crc32( "$buffer" );
    print "uboot env crc decimal: $crc_str\n" if $opt_v;
    $crc_str = sprintf "%08x", $crc_str;
    print "uboot env crc hex: $crc_str\n" if $opt_v;
    if ($env_endian ne 'big') {
        for ($i = 3; $i >= 0; $i--) {
            $crc_buffer .= pack("H2", substr($crc_str , $i * 2 , 2));
        }
    }
    else {
        for ($i = 0; $i < 4; $i++) {
            $crc_buffer .= pack("H2", substr($crc_str , $i * 2 , 2));
        }
    }
    open OUTFILE, ">", $of or
        die "Can not open $of: $! \n";
    binmode OUTFILE;
    print "uboot env: $buffer\n" if $opt_v;
    syswrite(OUTFILE, $crc_buffer, length($crc_buffer));
    syswrite(OUTFILE, $buffer, length($buffer));
    close OUTFILE;
}

###########################################################
# Name: parse_cfg
# Description: parse flash map config file, write data to
#              table @mtable
###########################################################
sub parse_cfg {
    my($cfg, $prefix) = @_;
    my $line = '';
	my @MATCHES = ();
    my $addr = "";
    my $filename = "";
    my $mapping = 0;
    my $i = 0;
	print "\nParsing flash map: $cfg with image path $prefix\n" if $opt_v;
	open INFILE, $cfg or
		die "Can not open $cfg: $! \n";
    while (chop($line = <INFILE>)) {
	    @MATCHES = $line =~ /^\/\//;
	    if ($#MATCHES > -1) {
		    # comments line, skip it.
		    next;
	    }
	    @MATCHES = $line =~ /^start /;
	    if ($#MATCHES > -1) {
    		($flash_start = $line) =~ s/^start +//;
    		print "Flash start from $flash_start\n" if $opt_v;
    		next;
    	}
    	@MATCHES = $line =~ /^size /;
    	if ($#MATCHES > -1) {
    		($flash_size = $line) =~ s/^size +//;
    		print "Flash size is $flash_size\n" if $opt_v;
    		next;
    	}
    	@MATCHES = $line =~ /^<mapping_table>/;
    	if ($#MATCHES > -1) {
    		$mapping = 1;
    		next;
    	}
    	@MATCHES = $line =~ /^<\/mapping_table>/;
    	if ($#MATCHES > -1) {
    		$mapping = 0;
    		next;
    	}
    	@MATCHES = $line =~ /^ *(0x[[:xdigit:]]+) (.*) *$/;
    	if (($mapping == 1) && ($#MATCHES > -1)) {
    		($addr = $line) =~ s/^ *(0x[[:xdigit:]]+) .*/$1/;
    		($filename = $line) =~ s/^ *(0x[[:xdigit:]]+) (.*) *$/$2/;
    		$addr =~ s/^\s+|\s+$//g;
    		$filename =~ s/^\s+|\s+$//g;
    		$mtable[$i][0] = hex $addr;
            if ($filename =~ "$uboot_envfile") {
    		    $mtable[$i][1] = $filename;
    		    $mtable[$i][2] = (-s $filename);
            }
            else {
    		    $mtable[$i][1] = $prefix."/".$filename;
    		    $mtable[$i][2] = (-s $prefix."/".$filename);
            }
            die "File $mtable[$i][1] is empty\n" if ($mtable[$i][2] == 0);
            if ($opt_v) {
                print "Block: $i ";
                printf "Address: 0x%08x ",$mtable[$i][0];
                printf "Image: %s ",$mtable[$i][1];
                printf "Size: %d\n",$mtable[$i][2];
            }
    		$i++;
    		next;
    	}
    }
    close INFILE;
}

###########################################################
# Name: cacul_space
# Description: calculate leading and trailing space, write 
#              data to table @mtable
###########################################################
sub cacul_space {
    my $next = 0x0;
    my $before = 0x0;
    my $i;
    print "\nCaculating leading and trailing space...\n" if $opt_v;
    for ($i = 0; $i <= $blocks; $i++) {
        if (($mtable[$i + 1][0]) && ($mtable[$i + 1][0] != 0)) {
    		$next = $mtable[$i + 1][0];
    	} else {
    		$next = (hex $flash_start) + (hex $flash_size);
    	}
    	die "Lack of space for $mtable[$i][1]:($mtable[$i][0] + $mtable[$i][2] > $next)" if ($mtable[$i][0] + $mtable[$i][2] > $next);
    	# Caculating leading space.
    	if ($i == 0) {
    		my $before = hex $flash_start;
    		die "Image $mtable[0][1] has lower addr than flash start." if ($mtable[0][0] < $before);
    		$mtable[0][3] = $mtable[0][0] - $before;
    	} else {
    		$mtable[$i][3] = 0;
    	}
    	# Caculating ending space.
    	$mtable[$i][4] = $next - $mtable[$i][0] - $mtable[$i][2];
    	print "Block: $i, Leading $mtable[$i][3] space, ending $mtable[$i][4] space\n" if $opt_v;
    }
}

sub add_uboottag {
	my($tag, $size, $if, $of) = @_;
	my $tag_maxlen = 100;
    my $tag_length = length($tag);
	my $tag_buffer = "";
	my $nul_buffer = "";
	my $buffer = "";
    my $i;
    print "Adding tag $tag for uboot $if, output is file $of\n" if $opt_v;
    for ($i = 0; $i < $tag_maxlen; $i ++) {
        $nul_buffer .= pack("H2", "FF");
        if ($i < $tag_length ) {
            $tag_buffer .= pack("A", substr($tag , $i , 1));
        }
        else {
            if ($i == $tag_length) {
                # append seperator
                $tag_buffer .= pack("H2", "00");
            }
            else {
                # append blank
                $tag_buffer .= pack("H2", "FF");
            }
        }
    }
    print "uboot tag: $tag_buffer\n" if $opt_v;
	open UBOOT_INFILE, $if or
		die "Can not open img $if!\n $! \n";
	binmode UBOOT_INFILE;
	read(UBOOT_INFILE, $buffer, $size );
	close UBOOT_INFILE;

	$i = -1;
	$i = rindex( $buffer , $nul_buffer);
	if ($i != -1 ) {
		$buffer =~ s/$nul_buffer/$tag_buffer/;
	}
	else {
        die "There is no enough space to write tag in $if\n";
	}

	open UBOOT_OUTFILE, ">", $of or
	    die "Can not open $of!\n $! \n";
	UBOOT_OUTFILE->autoflush(1);
	binmode UBOOT_OUTFILE;
	syswrite(UBOOT_OUTFILE, $buffer, length($buffer) );
	close UBOOT_OUTFILE;

	return index( $buffer, $tag_buffer);
}

###########################################################
# Name: fill_blank
# Description: write $length blank to $of, the blank block 
#              can include a string $tag. string $tag could
#              have a max size to 4096 byte.
###########################################################
sub fill_blank {
	my($of, $length, $tag) = @_;
	my $tag_maxlen = 4096;
	my $tag_length = 0;
	my $withtag = 0;
    my $buffer = "";
	my $nul_buffer = "" ;
    my $write_len = 0;
    my $i;
	if ($tag) {
	    $tag_length = length($tag);
		$withtag = 1;
	}
    print "length: $length, withtag: $withtag $tag\n" if $opt_v;
    # prepare part with tag (if any) (size: $tag_maxlen)
	for ($i = 0; $i < $tag_maxlen; $i ++) {
		$nul_buffer .= pack("H2", "FF"); 
		if ($i < $tag_length ) {
			$buffer .= pack("A", substr($tag , $i , 1));
		}
		else {
			if ($i == $tag_length) {
                # append seperator
				$buffer .= pack("H2", "00");
			}
			else {
                # append blank
				$buffer .= pack("H2", "FF");
			}	
		}
	}
	while ($length > 0) {
        # each time write no more than $tag_maxlen
		if ($length > $tag_maxlen) {
			$write_len = $tag_maxlen;
		} else {
			$write_len = $length;
		}
        # if tag exists, write it and unmark the flag
		if ($withtag == 1){
			syswrite($of, $buffer, $write_len);
			$withtag = 0 ;
		}
		else {
	        syswrite($of, $nul_buffer, $write_len);
		}
		$length -= $write_len;
	}
	return;
}
