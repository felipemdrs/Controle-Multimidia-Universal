
package com.embedded.controlemultimidiauniversal.sharedPreferences;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WeightStack {

    private final int SUPPORT_MAX;

    private int nElementsInserted = 0;

    String[] contentElements;

    public WeightStack(int supportMax) {
        SUPPORT_MAX = supportMax + 1;
        contentElements = new String[SUPPORT_MAX];
    }

    public WeightStack() {
        this(10);
    }

    public void create(String content) {
        String[] contents = content.split("\\,");

        List<String> list = Arrays.asList(contents);
        Collections.reverse(list);
        contents = (String[])list.toArray();

        nElementsInserted = 0;

        for (String element : contents) {
            add(element);
            System.out.println(element);
        }
    }

    public int size() {
        return nElementsInserted;
    }

    public void add(String element) {
        int posIndexElement = getIndexElementEquals(element);

        if (posIndexElement == -1) {
            posIndexElement = nElementsInserted;
            if (nElementsInserted < SUPPORT_MAX - 1)
                nElementsInserted++;
        }

        for (int i = posIndexElement - 1; i >= 0; i--) {
            contentElements[i + 1] = contentElements[i];
        }

        contentElements[0] = element;
    }

    public void swap(int from, int to) {
        String aux = contentElements[from];
        contentElements[from] = contentElements[to];
        contentElements[to] = aux;
    }

    private int getIndexElementEquals(String element) {
        int posIndexElement = -1;
        for (int i = 0; i < contentElements.length - 1; i++) {
            if (contentElements[i] != null) {
                if (contentElements[i].equals(element)) {
                    posIndexElement = i;
                    break;
                }
            }
        }
        return posIndexElement;
    }

    @Override
    public String toString() {
        String stringAux = "";
        for (int i = 0; i < nElementsInserted; i++) {
            stringAux += contentElements[i] + (i < nElementsInserted - 1 ? "," : "");
        }
        return stringAux;
    }

    public String[] getAll() {
        return contentElements;
    }

}
