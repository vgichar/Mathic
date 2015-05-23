package org.afg.mathic.world;

import org.afg.mathic.world.Modes.*;
import java.util.LinkedList;
import java.util.List;

public class ModeGroup {
	
	public List<Mode> modes;
	public String name;
	
	public ModeGroup(){
		modes = new LinkedList<Mode>();
	}
}
