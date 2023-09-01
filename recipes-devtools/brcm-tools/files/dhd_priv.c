
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <sys/ioctl.h>
#include <linux/socket.h>
#include <netpacket/packet.h>
#include <net/if.h>
#include <unistd.h>

#define VERSION	"1.2"

#define TAG "dhd_priv: "
#if defined(ANDROID)
#include <signal.h>
#include <errno.h>
#include <fcntl.h>
#include "cutils/misc.h"
#include "cutils/log.h"
#define DHD_PRINTF(...) {__android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__); printf(__VA_ARGS__);}
#else
#define DHD_PRINTF printf
#endif

typedef struct dhd_priv_cmd {
	char *buf;
	int used_len;
	int total_len;
} dhd_priv_cmd;

/*
terence 20161127
*/

int
main(int argc, char **argv)
{
	struct ifreq ifr;
	dhd_priv_cmd priv_cmd;
	int ret = 0;
	int ioctl_sock; /* socket for ioctl() use */
	char buf[500]="";
	int i=0;

	DHD_PRINTF(TAG "Version = %s\n", VERSION);

	DHD_PRINTF("argv: ");
	while (argv[i]) {
		DHD_PRINTF("%s ", argv[i]);
		i++;
	}
	DHD_PRINTF("\n");

	if (!argv[1]) {
		DHD_PRINTF("Please input right cmd\n");
		return 0;
	}

	while (*++argv) {
		strcat(buf, *argv);
		if (*(argv+1))
			strcat(buf, " ");
	}

	ioctl_sock = socket(PF_INET, SOCK_DGRAM, 0);
	if (ioctl_sock < 0) {
		DHD_PRINTF(TAG "socket(PF_INET,SOCK_DGRAM)\n");
		return -1;
	}

	memset(&ifr, 0, sizeof(ifr));
	memset(&priv_cmd, 0, sizeof(priv_cmd));
	strncpy(ifr.ifr_name, "wlan0", sizeof(ifr.ifr_name));

	priv_cmd.buf = buf;
	priv_cmd.used_len = 500;
	priv_cmd.total_len = 500;
	ifr.ifr_data = (void *)&priv_cmd;

	if ((ret = ioctl(ioctl_sock, SIOCDEVPRIVATE + 1, &ifr)) < 0) {
		DHD_PRINTF(TAG "failed to issue private commands %d\n", ret);
	} else {
		DHD_PRINTF(TAG "buf = %s, len = %d, ret = %d\n", buf, strlen(buf), ret);
	}

	close(ioctl_sock);
	return ret;
}

