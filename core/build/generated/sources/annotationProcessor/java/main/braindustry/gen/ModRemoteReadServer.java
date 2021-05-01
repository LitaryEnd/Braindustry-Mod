package braindustry.gen;

import mindustry.gen.RemoteReadServer;

import arc.util.io.Reads;
import mindustry.gen.Player;

/**
 * Autogenerated file. Do not modify!
 */
public class ModRemoteReadServer {
  public static void readPacket(Reads read, int id, Player player) {
    if(id == -1) {
      try {
        mindustry.gen.Unit unit = braindustry.io.ModTypeIO.readUnit(read);
        boolean inStealth = read.bool();
        braindustry.gen.ModNetServer.checkStealthStatus(player, unit, inStealth);
        braindustry.gen.ModCall.checkStealthStatus__forward(player.con, player, unit, inStealth);
      } catch (java.lang.Exception e) {
        throw new java.lang.RuntimeException("Failed to read remote method 'checkStealthStatus'!", e);
      }
    } else if(id == -2) {
      try {
        braindustry.gen.ModNetServer.damageAllUnits(player);
      } catch (java.lang.Exception e) {
        throw new java.lang.RuntimeException("Failed to read remote method 'damageAllUnits'!", e);
      }
    } else if(id == -4) {
      try {
        braindustry.gen.ModNetServer.healAllUnits(player);
      } catch (java.lang.Exception e) {
        throw new java.lang.RuntimeException("Failed to read remote method 'healAllUnits'!", e);
      }
    } else if(id == -5) {
      try {
        braindustry.gen.ModNetServer.killAllUnits(player);
      } catch (java.lang.Exception e) {
        throw new java.lang.RuntimeException("Failed to read remote method 'killAllUnits'!", e);
      }
    } else if(id == -7) {
      try {
        mindustry.gen.Unit target = braindustry.io.ModTypeIO.readUnit(read);
        braindustry.input.ModInput.requestUnitPayload(player, target);
      } catch (java.lang.Exception e) {
        throw new java.lang.RuntimeException("Failed to read remote method 'requestUnitPayload'!", e);
      }
    } else if(id == 45) {
      try {
        java.lang.String message = mindustry.io.TypeIO.readString(read);
        braindustry.core.ModNetClient.sendChatMessage(player, message);
      } catch (java.lang.Exception e) {
        throw new java.lang.RuntimeException("Failed to read remote method 'sendChatMessage'!", e);
      }
    } else if(id == -8) {
      try {
        mindustry.type.UnitType type = mindustry.io.TypeIO.readUnitType(read);
        braindustry.gen.ModNetServer.setNewUnit(player, type);
      } catch (java.lang.Exception e) {
        throw new java.lang.RuntimeException("Failed to read remote method 'setNewUnit'!", e);
      }
    } else if(id == -10) {
      try {
        mindustry.gen.Unit unit = braindustry.io.ModTypeIO.readUnit(read);
        boolean inStealth = read.bool();
        float value = read.f();
        braindustry.gen.ModNetServer.setStealthStatus(player, unit, inStealth, value);
      } catch (java.lang.Exception e) {
        throw new java.lang.RuntimeException("Failed to read remote method 'setStealthStatus'!", e);
      }
    } else if(id == -11) {
      try {
        mindustry.game.Team team = braindustry.io.ModTypeIO.readTeam(read);
        braindustry.gen.ModNetServer.setTeam(player, team);
      } catch (java.lang.Exception e) {
        throw new java.lang.RuntimeException("Failed to read remote method 'setTeam'!", e);
      }
    } else if(id == -12) {
      try {
        mindustry.gen.Unit unit = braindustry.io.ModTypeIO.readUnit(read);
        braindustry.gen.ModNetServer.setUnit(player, unit);
      } catch (java.lang.Exception e) {
        throw new java.lang.RuntimeException("Failed to read remote method 'setUnit'!", e);
      }
    } else if(id == -13) {
      try {
        float x = read.f();
        float y = read.f();
        float radius = read.f();
        arc.graphics.Color first = mindustry.io.TypeIO.readColor(read);
        arc.graphics.Color second = mindustry.io.TypeIO.readColor(read);
        float fromRadius = read.f();
        float toRadius = read.f();
        braindustry.core.ModNetClient.showTeleportCircles(player, x, y, radius, first, second, fromRadius, toRadius);
        braindustry.gen.ModCall.showTeleportCircles__forward(player.con, player, x, y, radius, first, second, fromRadius, toRadius);
      } catch (java.lang.Exception e) {
        throw new java.lang.RuntimeException("Failed to read remote method 'showTeleportCircles'!", e);
      }
    } else if(id == -14) {
      try {
        mindustry.type.UnitType type = mindustry.io.TypeIO.readUnitType(read);
        float x = read.f();
        float y = read.f();
        int amount = read.i();
        boolean spawnerByCore = read.bool();
        mindustry.game.Team team = braindustry.io.ModTypeIO.readTeam(read);
        braindustry.gen.ModNetServer.spawnUnits(player, type, x, y, amount, spawnerByCore, team);
      } catch (java.lang.Exception e) {
        throw new java.lang.RuntimeException("Failed to read remote method 'spawnUnits'!", e);
      }
    } else if(id == -16) {
      try {
        mindustry.gen.Building build = mindustry.io.TypeIO.readBuilding(read);
        java.lang.Object value = braindustry.io.ModTypeIO.readObject(read);
        braindustry.input.ModInput.tileConfig(player, build, value);
        braindustry.gen.ModCall.tileConfig__forward(player.con, player, build, value);
      } catch (java.lang.Exception e) {
        throw new java.lang.RuntimeException("Failed to read remote method 'tileConfig'!", e);
      }
    } else if(id == -17) {
      try {
        arc.math.geom.Vec2 pos = braindustry.io.ModTypeIO.readVec2(read);
        braindustry.gen.ModNetServer.tpAllUnits(player, pos);
      } catch (java.lang.Exception e) {
        throw new java.lang.RuntimeException("Failed to read remote method 'tpAllUnits'!", e);
      }
    } else {
      throw new braindustry.net.ModNet.SuccessfulException();
    }
  }
}
