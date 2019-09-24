CC ?= $(CROSS_COMPILE)gcc

all: rtk_hciattach

rtk_hciattach: hciattach.c hciattach_rtk.o
	$(CC) -o rtk_hciattach hciattach.c hciattach_rtk.o $(CFLAGS) $(LDFLAGS)

hciattach_rtk.o:hciattach_rtk.c
	$(CC) -c hciattach_rtk.c $(CFLAGS) $(LDFLAGS)

clean:
	rm -f *.o  rtk_hciattach tags cscope.*

tags: FORCE
	ctags -R
	find ./ -name "*.h" -o -name "*.c" -o -name "*.cc" -o -name "*.cpp" > cscope.files
	cscope -bkq -i cscope.files
PHONY += FORCE
FORCE:
