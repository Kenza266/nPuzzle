package com.backend;


import java.lang.reflect.InvocationTargetException;

public class PSOimplementation {

    public final int numParticles = 1000; //Number of particles in swarm
    public final int maxIterations = 5000; //Max number of iterations
    public final double c1 = 1.2; //Cognitive coefficient
    public final double c2 = 1.6; //Social coefficient
    public final double w = 1.0; //Inertia coefficient
    public double r1; //Random vector 1
    public double r2;  //Random vector 2
    public long best;
    Particle[] particles; //Array to hold all particles

    public PSOimplementation() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        //PSO algorithm

        particles = new Particle[numParticles];
        PSOEngine PSO = new PSOEngine(numParticles, maxIterations, c1, c2, w);

        //Initialize particles
        PSO.initParticles(particles);

        //PSO loop
        int numIter = 0;
        while (numIter < maxIterations) {
            // Evaluate fitness of each particle
            for (int i = 0; i < numParticles; i++) {
                particles[i].fitness = PSO.evaluateFitness(particles[i].position);

                //update personal best position
                if (particles[i].fitness <= PSO.evaluateFitness(particles[i].personalBest)) {
                    particles[i].personalBest = particles[i].position;
                }
            }
            //Find best particle in set
            best = PSO.findBest(particles);

            //Initialize the random vectors for updates

            r1 = Math.random();
            r2 = Math.random();


            //Update the velocity and position vectors
            for (int i = 0; i < numParticles; i++) {
                PSO.updateVelocity(particles[i], best, r1, r2);
                PSO.updatePosition(particles[i]);
            }
            numIter++;
            System.out.println("iteration="+numIter);
            for (int i = 0; i < particles.length; i++) {
                /*System.out.println("------particle["+i+"]----------");
                System.out.println("position="+particles[i].position);
                System.out.println("velocity="+particles[i].velocity);
                System.out.println("fitness="+particles[i].fitness);
                System.out.println("state:");
                particles[i].currentState.display();*/
                if(particles[i].currentState.getData()==123804765){
                    System.out.println("Found");
                    return;
                }
            }

        }

        //Print the best solution

    }


    /**
     * Helped method to print an array as a vector
     * @param a The given 1-D array
     */
    public void print (int a) {
        System.out.print("< ");
        System.out.print(a  + " ");
        System.out.println(" > ");

    }

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        PSOimplementation p = new PSOimplementation();
    }

}