package com.backend;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class geneticSolver {

    private Puzzle puzzle;
    private int initialPopulation;
    private double mutationChance;
    private double crossOverRate;
    private int elite;
    public List<Chromosome> population, childPopulation, newPopulation;
    private Chromosome best;
    private double error, total, probability;
    List<Double> roulette;

    public geneticSolver(Puzzle puzzle, int initialPopulation, double mutationChance, double crossOverRate, double eliteRate) {
        this.puzzle = puzzle;
        this.initialPopulation = initialPopulation;
        this.mutationChance = mutationChance;
        this.crossOverRate = crossOverRate;
        this.error = 100000;
        this.elite = (int) (eliteRate * this.initialPopulation);
        this.population = new ArrayList<>();
        this.childPopulation = new ArrayList<>();
    }

    public boolean after(State goal, int i, int j){
        return goal.getIndex(i) > goal.getIndex(j);
    }

    public int getInversions(State start, State goal) {
        int inversion=0, x, y;

        for(int i=(int) Math.pow(10, puzzle.getSize()*puzzle.getSize()-1); i>0; i=i/10) {
            x = ((start.getData() - start.getData() % i) / i) % 10;
            if (x != 0)
                for(int j=i/10; j>0; j=j/10) {
                    y = ((start.getData() - start.getData() % j) / j) % 10;
                    if (y != 0 && after(goal, x, y)) {
                        inversion++;
                    }
                }
        }
        return inversion;
    }

    public void initPopulation() {
        for (int i=0; i<this.initialPopulation; i++) {
            this.population.add(new Chromosome(this.puzzle.getStart()));
        }
        mutate(1, 1, population);
        correct(this.population);
        improve(this.population);
        computeCosts(this.population);
    }

    public void initRoulette(List<Chromosome> individuals) {
        roulette = new ArrayList<>();
        total = 0;
        for(Chromosome p : individuals){
            total += 1/(0.000001 + p.getError());
            roulette.add(total);
        }
    }

    public int selectRoulette() {
        probability = (double) (Math.random() * total);
        for(int j=0; j<roulette.size(); j++){
            if(probability < roulette.get(j)){
                return j;
            }
        }
        return -1;
    }

    public void keepBests() {
        this.population.sort((Chromosome a , Chromosome b) -> Double.valueOf(a.getError()).compareTo(b.getError()));
        this.childPopulation.sort((Chromosome a , Chromosome b) -> Double.valueOf(a.getError()).compareTo(b.getError()));
        Chromosome c;
        this.initRoulette(this.childPopulation);
        newPopulation = new ArrayList<Chromosome>(this.population.subList(0, this.elite));
        for(int i=0; i<this.initialPopulation-this.elite; i++) {
            c = this.childPopulation.get(this.selectRoulette());
            newPopulation.add(c);
        }
        this.population = newPopulation;
    }

    public boolean isSolution(State state) {
        return this.puzzle.getGoal().getData() == state.getData();
    }

    public Chromosome solve(int maxIteration) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        System.out.println("----------------------------------------------------");
        List<Double> loss = new ArrayList<>();
        int iteration = 0, stable = 0;
        double prevError = 100000;
        while (iteration<maxIteration){
            this.crossOvers();
            this.mutate(0.1*this.error, (double) (stable/(0.9*maxIteration)), childPopulation);
            this.correct(this.childPopulation);
            this.improve(this.childPopulation);
            this.computeCosts(this.childPopulation);
            this.keepBests();
            if((Math.pow(prevError - this.best.getError(), 2) < 0.001))
                stable += 1;
            else
                stable = 0;
            if((double) (stable/(0.9*maxIteration))>0.002){
                //System.out.println("Big mutation");
                //this.mutate(1, -1, this.population);
                //this.improve(this.population);
                //this.validate(this.population);
                //this.computeCosts(this.population);
                //this.keepBests();
            }
            loss.add(this.best.getError());
            prevError = this.best.getError();
            this.display(iteration);
            if (this.isSolution(this.best.getState())){
                System.out.println("Solution found");
                best.getGene().forEach(g -> System.out.print(g+"\t"));
                //loss.forEach(l -> System.out.println(l));
                return best;
            }
            iteration += 1;
        }
        return null;
    }

    private void improve(List<Chromosome> pop) {
        for (Chromosome gene : pop){
            gene.improve();
        }
    }

    private void correct(List<Chromosome> pop) {
        for(Chromosome gene :pop){
            gene.correctGene(this.puzzle.getStart());
        }
    }

    private void computeCosts(List<Chromosome> pop) {
        double sum = 0, min = 100000;
        for (Chromosome gene : pop){
            try {
                gene.computeCost(this.puzzle.getGoal());
                sum += gene.getError();
                //System.out.println("cost "+gene.getError()+" sum "+sum);
                if(gene.getError() < min){
                    min = gene.getError();
                    this.best = gene;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            };
        }
        this.error = (float) sum/pop.size();
        //System.out.println("In computeCost, error "+this.error+", best "+this.best.getError());
    }

    private void crossOvers() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        int occurrences = (int) (this.crossOverRate * this.population.size());
        Chromosome[] children;
        Chromosome parent1, parent2;
        this.childPopulation = new ArrayList<>();
        int size;
        List<Chromosome> fertileGenes = new ArrayList<Chromosome>(this.population);
        size = fertileGenes.size();
        //System.out.println("Fertile "+size);

        initRoulette(fertileGenes);

        for(int i=0; i<occurrences; i++){
            //initRoulette(fertileGenes);
            parent1 = fertileGenes.get(selectRoulette());
            parent2 = fertileGenes.get(selectRoulette());
            //fertileGenes.remove(parent1);
            //fertileGenes.remove(parent2);

            children = Chromosome.crossOver(parent1, parent2);

            //if (children[0].validateGene(this.puzzle.getStart()) != null
            //        && children[0].computeCost(this.puzzle.getGoal()) < this.error)
            this.childPopulation.add(children[0]);
            //System.out.println(this.error);
            //if(children[1].validateGene(this.puzzle.getStart()) != null
            //        && children[1].computeCost(this.puzzle.getGoal()) < this.error)
            this.childPopulation.add(children[1]);
        }
    }

    private void mutate(double chance, double stable, List<Chromosome> pop) {
        //System.out.println("Mutate with chance= "+(this.mutationChance + chance)+" stable= "+stable);
        //float chance = this.mutationChance + this.computeSimilarity();
        for (Chromosome gene : pop){
            if (Math.random() < this.mutationChance + chance) {
                //System.out.println("\nBefore mutation");
                //gene.getGene().forEach(g -> System.out.print(g + "\t"));
                gene.mutate(stable);
                //System.out.println("\nAfter mutation");
                //gene.getGene().forEach(g -> System.out.print(g + "\t"));
            }
            //gene.improve();
        }
    }

    private void display(int iteration) {
        int len = 0;
        for(Chromosome c:this.population){
            if(c.getGene().size() > len)
                len=c.getGene().size();
        }
        System.out.println("Iteration: "+(iteration+1)+", population of: "+this.population.size()+", longest path: "+len);
        System.out.println("Error: "+this.error+", best error: "+this.best.getError());
        //this.best.getState().display();
        best.getGene().forEach(g -> System.out.print(g+"\t"));
        System.out.println(/*"inv = "+getInversions(best.getState(), this.puzzle.getGoal())*/);
        /*System.out.println("Population: ");
        this.population.forEach(c -> {
            System.out.print("Gene: ");
            c.getGene().forEach(g -> System.out.print(g+"\t"));
            //c.getState().display();
            System.out.println("error: "+c.getError());
        });*/
        System.out.println("----------------------------------------------------");
    }

}
