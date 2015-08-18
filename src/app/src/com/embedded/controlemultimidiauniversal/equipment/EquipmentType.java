
package com.embedded.controlemultimidiauniversal.equipment;

/**
 * Lista de equipamentos a serem controlados.
 * 
 * @author felipemm
 */
public enum EquipmentType {
    TV("tv"), SOM("som");

    String equipment;

    EquipmentType(String equipment) {
        this.equipment = equipment;
    }

    public String toString() {
        return equipment;
    }
}
