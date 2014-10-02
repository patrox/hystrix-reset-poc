hystrix-reset-poc
=================

Simple proof-of-concept for fixing an issue with hystrix threads stuck in WAITING status

After each call to HystrixCommand.execute() you need to call Hystrix.reset(),
otherwise the thread created by the execute() method will be stuck in WAITING status.

This can be easilly identified, by running:

jstack <PID> | grep hystrix

the number of lines returned by the above command, will match the number of calls to HystrixCommand.execute(),
not followed by Hystrix.reset().

```user@host:~$ jstack 4741 | grep hystrix```
```hystrix-hystrix_reset_poc-2 prio=10 ... waiting on condition ...```
```hystrix-hystrix_reset_poc-1 prio=10 ... waiting on condition ...```

The example was heavily inspired by the description of this issue under the following URL:

https://github.com/Netflix/Hystrix/issues/102
