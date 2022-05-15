package com.backend;


import java.lang.reflect.InvocationTargetException;

public class PSOimplementation {

    public final int numParticles = 5000; //Number of particles in swarm
    public final int maxIterations = 5000; //Max number of iterations
    public final double c1 = 1.49; //Cognitive coefficient
    public final double c2 = 1.49; //Social coefficient
    public final double w = 0.72; //Inertia coefficient
    public double r1; //Random vector 1
    public double r2;  //Random vector 2
    public long best;
    Particle[] particles; //Array to hold all particles

    public Particle solve(int state) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        particles = new Particle[numParticles];
        PSOEngine PSO = new PSOEngine(numParticles, state, maxIterations, c1, c2, w);

        PSO.initParticles(particles);

        //PSO loop
        int numIter = 0;
        while (numIter < maxIterations) {
            for (int i = 0; i < numParticles; i++) {
                particles[i].fitness = PSO.evaluateFitness(particles[i].position);

                if (particles[i].fitness <= PSO.evaluateFitness(particles[i].personalBest)) {
                    particles[i].personalBest = particles[i].position;
                }
            }
            best = PSO.findBest(particles);

            r1 = Math.random();
            r2 = Math.random();

            for (int i = 0; i < numParticles; i++) {
                PSO.updateVelocity(particles[i], best, r1, r2);
                PSO.updatePosition(particles[i]);
            }
            numIter++;
            //System.out.println("iteration="+numIter);
            //System.out.println("------particle["+i+"]----------");
            System.out.println("position="+best);
            System.out.println("fitness="+(float) PSO.evaluateFitness(Particle.decode(best))/10);
            for (int i = 0; i < particles.length; i++) {
                /*System.out.println("------particle["+i+"]----------");
                System.out.println("position="+particles[i].position);
                System.out.println("velocity="+particles[i].velocity);
                System.out.println("fitness="+particles[i].fitness);
                System.out.println("state:");
                particles[i].currentState.display();*/
                if(particles[i].currentState.getData()==123804765){
                    System.out.println("Found");
                    System.out.println(particles[i].position);
                    return particles[i];
                }
            }
        }
        return null;
    }

    public void print (int a) {
        System.out.print("< ");
        System.out.print(a  + " ");
        System.out.println(" > ");
    }

}