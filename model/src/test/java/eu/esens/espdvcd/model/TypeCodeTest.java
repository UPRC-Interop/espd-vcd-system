package eu.esens.espdvcd.model;

import org.junit.Test;


public class TypeCodeTest {
    
    @Test
    public void typeCodeSplitTest() {
        String typeCode = "CRITERION.EXCLUSION.CONVICTIONS.PARTICIPATION_IN_CRIMINAL_ORGANISATION";
        String[] ar= typeCode.split("\\.",4);
        StringBuilder sb = new StringBuilder();
        int size = 3;
        if (ar.length < size) size = ar.length;
        for (int i=0; i<size; i++) {
            sb.append(ar[i]);
            sb.append(".");
        }
        System.out.println(sb.delete(sb.lastIndexOf("."), sb.length()));
    }

}
