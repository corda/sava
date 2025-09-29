package software.sava.core.internal;

import java.util.HashMap;
import java.util.HashSet;

public class Java19Support {
  public static <K, V> HashMap<K, V> newHashMap(int numMappings) {
    if (numMappings < 0) {
      throw new IllegalArgumentException("Negative number of mappings: " + numMappings);
    }
    return new HashMap<>(calculateHashMapCapacity(numMappings));
  }

  public static <T> HashSet<T> newHashSet(int numElements) {
    if (numElements < 0) {
      throw new IllegalArgumentException("Negative number of elements: " + numElements);
    }
    return new HashSet<>(calculateHashMapCapacity(numElements));
  }

  private static int calculateHashMapCapacity(int numElements) {
    return (int)Math.ceil(numElements / 0.75);
  }
}
