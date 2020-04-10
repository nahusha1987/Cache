# Cache
Sample Implementation of in memory cache

## TTL Cache
### Design Consideration

Assumptions :
The Cache is a bounded one which means it has a predefined size and cannot grow indefinitely.
In case the size is not specified by the user, a default size of 10 is considered.

There might be 2 ways in which the Cache may be implemented.
The `Time To Live` may be specified for an individual element or it may be
common for all the elements of the cache.

In case it is specified per element, then each value object must run it's own
<code>TimerTask</code> to evict itself from the cache. This results in sub optimal
performance since the number of eviction threads would be equal to the size of the cache.
Also, when the cache is full, it is required to find out if the incoming entry should replace
an element on the cache or should it be ignored.

In case `TTL` is specified per cache, then there can be <code>TimerTask</code> which will
be run by a <code>Timer</code> when the next element in the cache is supposed to be evicted.
To figure out which is the next element scheduled to be evicted a min heap (<code>PriorityQueue<E></code>)
is used. This again would result in a sub optimal performance since there is a need to run a seperate
job / thread for the sake of eviction.
A better approach for eviction strategy would be to check if the element needs to be evicted during the
<code>get</code> and <code>put</code> operations.
1. During <code>get</code>, in case an element has expired its TTL, then the element is removed from the
<code>Map</code> and the <code>PriorityQueue</code> and null is retured.
2. During <code>get</code>, in case the element has not expired, then the <code>lastAccessed</code> modifier
for the element is reset back to the original TTL value and the <code>PriorityQueue</code> is reordered.
3. During <code>put</code>, in case there is still space left on the cache, then the value is inserted in the
<code>Map</code> and on the <code>PriorityQueue</code>
4. During <code>put</code>, in case the cache is full, the oldest element (first element from Heap) is removed
from both Map and PriorityQueue and the newer element is inserted