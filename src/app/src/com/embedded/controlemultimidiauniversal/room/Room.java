package com.embedded.controlemultimidiauniversal.room;

import java.util.ArrayList;
import java.util.List;

import com.embedded.controlemultimidiauniversal.equipment.Equipment;

public class Room implements Comparable<Room> {
	private List<Equipment> listEquipments;

	private String name;

	public Room(String name) {
		this.listEquipments = new ArrayList<Equipment>();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean addEquipment(Equipment equipment) {
		return listEquipments.add(equipment);
	}

	public Equipment getEquipmentByName(String name) {
		for (Equipment equipTemp : listEquipments) {
			if (equipTemp.getName().equals(name)) {
				return equipTemp;
			}
		}
		return null;
	}

	@Override
	public int compareTo(Room another) {
		return getName().compareTo(another.getName());
	}
}
