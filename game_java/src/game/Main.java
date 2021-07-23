package game;

import java.util.*;
import static game.InOutUtils.readStringsFromInputStream;
import static game.ProcessUtils.UTF_8;

/**
 * Main samplegame class.
 */
public class Main {

    public static void main(String[] args) {
        List<String> input = readStringsFromInputStream(System.in, UTF_8);
        if(!input.isEmpty()){
            Round round = new Round(input);
            printMovingGroups(makeMove(round));
        }
        System.exit(0);
    }

    private static List<MovingGroup> makeMove(Round round) {
        List<MovingGroup> movingGroups = new ArrayList<>();
        // Место для Вашего кода.
        try {
            List<Planet> planets = round.getOwnPlanets();
            List<Planet> planetsToDef = new ArrayList();
            if (round.getCurrentStep() < 4) {
                if (!round.getNoMansPlanets().isEmpty()) {
                    Planet home1= getHomePlanet(round);
                    List<Planet> noMn = getNoMnPlanets(round);
                    if (!noMn.isEmpty()) {
                        noMn.sort((o1, o2) -> {
                            return round.getDistanceMap()[home1.getId()][o1.getId()] - round.getDistanceMap()[home1.getId()][o2.getId()];
                        });
                        for (int i = 0; i < noMn.size(); i++) {
                            if (home1.getPopulation() > noMn.get(i).getPopulation() + 1) {
                                MovingGroup grup = new MovingGroup();
                                grup.setFrom(home1.getId());
                                grup.setTo(noMn.get(i).getId());                              grup.setCount(noMn.get(i).getPopulation() + 1);
                                movingGroups.add(grup);
                            }
                        }
                    }
                }
            } else {
                try {
                    List<MovingGroup> enemAttack = round.getAdversarysMovingGroups();
                    Planet planetIsAtt;
                    List<Planet> planets1 = round.getOwnPlanets();
                    List<MovingGroup> mGrupDef = round.getOwnMovingGroups();
                    boolean flag1 = true;
                    for (int j = 0; j < enemAttack.size(); j++) {
                        planetIsAtt = round.getPlanets().get(enemAttack.get(j).getTo());
                        if ((planetIsAtt.getOwnerTeam() == round.getTeamId()) &&
                                (planetIsAtt.getPopulation() + enemAttack.get(j).getStepsLeft() * planetIsAtt.getReproduction() <= enemAttack.get(j).getCount())) {
                            Planet planetToAttack1 = planetIsAtt;
                            for(MovingGroup element:mGrupDef)
                                if (element.getTo() == planetIsAtt.getId())
                                    flag1 =false;
                            if(flag1) {
                                boolean flag2 = true;
                                planets1.sort((o1, o2) -> {
                                    return round.getDistanceMap()[o1.getId()][planetToAttack1.getId()] - round.getDistanceMap()[o2.getId()][planetToAttack1.getId()];
                                });
                                for (Planet element : planets1) {
                                    if(flag2){
                                        if ((element.getPopulation() > enemAttack.get(j).getCount())) {
                                            MovingGroup group = new MovingGroup();
                                            group.setFrom(element.getId());
                                            group.setTo(planetIsAtt.getId());
                                            group.setCount(enemAttack.get(j).getCount());
                                            movingGroups.add(group);
                                            flag2 = false;                                            round.getPlanets().get(element.getId()).setPopulation(round.getPlanets().get(element.getId()).getPopulation() - enemAttack.get(j).getCount());                                            planetsToDef.add(planetIsAtt);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (NullPointerException e) {
                }
                List<Planet> otherPlanets = new ArrayList();
                List<Planet> sentToNoMan = new ArrayList<>();
                boolean flag3 = true;
                for (int i = 0; i < round.getPlanets().size(); i++) {
                    if (round.getPlanets().get(i).getOwnerTeam() != round.getTeamId()) {
                        try {
                            flag3 = true;
                            int k = 0;
                            Planet planetToAttack = round.getPlanets().get(i);
                            planets.sort((o1,o2) -> {return round.getDistanceMap()[o1.getId()][planetToAttack.getId()] - round.getDistanceMap()[o2.getId()][planetToAttack.getId()];});
                            int j = 0, countS = 1;
                            while ((j < planets.size())) {
                                if (!planetsToDef.contains(planets.get(j))) {
                                    if ((round.getPlanets().get(i).getOwnerTeam() == -1) && (planets.get(j).getPopulation() > round.getPlanets().get(i).getPopulation() + 1)&&(!sentToNoMan.contains(round.getPlanets().get(i)))) {
                                        MovingGroup group = new MovingGroup();                                       group.setFrom(planets.get(j).getId());                                        group.setTo(round.getPlanets().get(i).getId());                                       group.setCount(round.getPlanets().get(i).getPopulation() + 1);                                     planets.get(j).setPopulation(planets.get(j).getPopulation() - round.getPlanets().get(i).getPopulation() - 1);                                        sentToNoMan.add(round.getPlanets().get(i));
                                        movingGroups.add(group);
                                    } else if ((planets.get(j).getPopulation() >                                            (round.getPlanets().get(i).getPopulation()/countS +  round.getDistanceMap()[round.getPlanets().get(i).getId()][planets.get(j).getId()/countS])+countS)){

                                        MovingGroup group = new MovingGroup();                                        group.setFrom(planets.get(j).getId());                                        group.setTo(round.getPlanets().get(i).getId());                                        group.setCount(round.getPlanets().get(i).getPopulation()/countS + round.getDistanceMap()[round.getPlanets().get(i).getId()][planets.get(j).getId()/countS]+countS);
                                        movingGroups.add(group);                                       planets.get(j).setPopulation(planets.get(j).getPopulation() - (round.getPlanets().get(i).getPopulation()/countS + round.getDistanceMap()[round.getPlanets().get(i).getId()][planets.get(j).getId()/countS]+countS));
                                        countS++;
                                    }
                                }
                                j++;
                            }
                        } catch (NullPointerException e) {
                            int j = 0, countS = 1;
                            while ((flag3) && (j < planets.size())) {
                                if (!planetsToDef.contains(planets.get(j))) {
                                    if ((round.getPlanets().get(i).getOwnerTeam() == -1) && (planets.get(j).getPopulation() > round.getPlanets().get(i).getPopulation() + 1)&&(!sentToNoMan.contains(round.getPlanets().get(i)))) {
                                        MovingGroup group = new MovingGroup();                                        group.setFrom(planets.get(j).getId());                                        group.setTo(round.getPlanets().get(i).getId());                                        group.setCount(round.getPlanets().get(i).getPopulation() + 1);                                        sentToNoMan.add(round.getPlanets().get(i));
                                        movingGroups.add(group);                                        planets.get(j).setPopulation(planets.get(j).getPopulation() - round.getPlanets().get(i).getPopulation() - 1);
                                    } else if ((planets.get(j).getPopulation() >                                            (round.getPlanets().get(i).getPopulation()/countS + round.getDistanceMap()[round.getPlanets().get(i).getId()][planets.get(j).getId()/countS])+countS)){
                                        MovingGroup group = new MovingGroup();                                        group.setFrom(planets.get(j).getId());                                        group.setTo(round.getPlanets().get(i).getId());

                                        group.setCount(round.getPlanets().get(i).getPopulation()/countS + round.getDistanceMap()[round.getPlanets().get(i).getId()][planets.get(j).getId()/countS]+countS);
                                        movingGroups.add(group);                                        planets.get(j).setPopulation(planets.get(j).getPopulation() - (round.getPlanets().get(i).getPopulation()/countS + round.getDistanceMap()[round.getPlanets().get(i).getId()][planets.get(j).getId()/countS]+countS));
                                        countS++;
                                    }
                                }
                                j++;
                            }
                        }
                    }
                }
            }
        }catch(NullPointerException e){}
        return movingGroups;
    }
    private static Planet getHomePlanet(Round round) {
        Planet home1;
        if(round.getTeamId() == 0)
            home1 = round.getPlanets().get(0);
        else
            home1 = round.getPlanets().get(round.getPlanetCount()-1);
        return home1;
    }
    private static List<Planet> getNoMnPlanets(Round round) {
        List<Planet> noMans = round.getNoMansPlanets();
        List<Planet> rplanets = new ArrayList<>();
        boolean flag3;
        try {
            List<MovingGroup> myGroups = round.getOwnMovingGroups();
            for(int j = 0;j<noMans.size();j++){
                flag3 = true;
                for(int i = 0;i<myGroups.size();i++){
                    if(myGroups.get(i).getTo() == noMans.get(j).getId())
                        flag3 = false;
                }
                if(flag3)
                    rplanets.add(noMans.get(j));
            }
            return rplanets;
        }catch(NullPointerException e){
            return noMans;
        }
    }
    private static void printMovingGroups(List<MovingGroup> moves) {
        System.out.println(moves.size());
        moves.forEach(move -> System.out.println(move.getFrom() + " " + move.getTo() + " " + move.getCount()));
    }
}

