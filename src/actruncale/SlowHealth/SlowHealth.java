/*
* Copyright (C) 2011 <adam@truncale.net>
*
* This file is part of the Bukkit plugin SlowHealth.
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston,
* MA 02111-1307, USA.
*/

package actruncale.SlowHealth;

import java.io.IOException;
import java.util.Timer;
import java.util.Properties;
import java.io.FileInputStream;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class SlowHealth extends JavaPlugin
{
  private Timer m_Timer = new Timer(true);
  private int HP = 0;
  private double rate = 1;
  private int healAmount = 1;
  private int maxHeal = 20;
  private int maxHurt = 0;
  private int minAltitude = 0;
  
  public void onEnable()
  {
    
	PluginDescriptionFile pdfFile = this.getDescription();
    
    Properties props = new Properties();
	try {
		props.load(new FileInputStream("server.properties"));
		if (props.containsKey("regen-rate")) 
			rate = Double.parseDouble(props.getProperty("regen-rate"));
		if (props.containsKey("regen-amount")) 
			healAmount = Integer.parseInt(props.getProperty("regen-amount"));
		if (props.containsKey("regen-max")) 
			maxHeal = Integer.parseInt(props.getProperty("regen-max"));
		if (props.containsKey("regen-min")) 
			maxHurt = Integer.parseInt(props.getProperty("regen-min"));
		if (props.containsKey("min-altitude"))
			minAltitude = Integer.parseInt(props.getProperty("min-altitude"));
	} catch (IOException ioe) {
		System.out.println( pdfFile.getName() + " : ERROR CAN/'T FIND server.properties" );
	}
	System.out.print( pdfFile.getName() +" "+ pdfFile.getVersion() + " enabled! Rate: " + rate + "s | Amount: " + healAmount );
	if (healAmount >= 0)
		System.out.println(" | Max: " + maxHeal);
	else 
		System.out.println(" | Min: " + maxHurt);
    m_Timer.schedule(new SimpleTimer(this), 0, (long)(rate*1000));
  }

  public void onDisable()
  {
    System.out.println("Goodbye world!");
    m_Timer.cancel();
  }

  public void handleHealth()
  {
	  for ( Player player : getServer().getOnlinePlayers() )
	  {
		  HP = player.getHealth();
		  if(HP<0)
			  player.setHealth(0);

		  if (player.getLocation().getBlockY() < minAltitude){
			  continue;
		  }
		  
		  if(HP<maxHeal && HP>0 && healAmount>0)
		  {
			  player.setHealth(HP+healAmount);
			  if(player.getHealth()>20)
				  player.setHealth(20);
		  }
		  else if(HP<=20 && HP>maxHurt && healAmount<0)
		  {
			  player.setHealth(HP+healAmount);
		  }
    	}
    }
      
  }