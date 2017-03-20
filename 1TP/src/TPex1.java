//Pour la configuration de eclispe pour connecter JAVA, veuillez consulter le site www-01.ibm.com/support/docview.wss?uid=swg21449776
import ilog.concert.*;
import ilog.cplex.*;


public class TPex1 {

   public static void main(String[] args) {
      try {
         IloCplex cplex = new IloCplex();

         IloNumVar[][] var = new IloNumVar[1][];
         IloRange[][]  rng = new IloRange[1][];
         populateByRow(cplex, var, rng);


         // write model to file
         cplex.exportModel("lpex1.lp");

         // solve the model and display the solution if one was found
         if ( cplex.solve() ) {
            double[] x     = cplex.getValues(var[0]);
            cplex.output().println("Solution status = " + cplex.getStatus());
            cplex.output().println("Solution value  = " + cplex.getObjValue());
            cplex.output().println(x[0]);
            cplex.output().println(x[1]);
            cplex.output().println(x[2]);

    
         }
         cplex.end();
		
	}
       catch (IloException e) {
         System.err.println("Concert exception '" + e + "' caught");
      }
   }


   // linear program:
   //
   //    Maximize
   //     x1 + 2 x2 + 3 x3
   //    Subject To
   //     - x1 + x2 + x3 <= 20
   //     x1 - 3 x2 + x3 <= 30
   //    Bounds
   //     0 <= x1 <= 40
   //    End
   //
   // using the IloMPModeler API

   static void populateByRow(IloMPModeler model,
                             IloNumVar[][] var,
                             IloRange[][] rng) throws IloException {
      double[]    lb      = {0.0, 0.0, 0.0};
      double[]    ub      = {40.0, Double.MAX_VALUE, Double.MAX_VALUE};
      String[]    varname = {"x1", "x2", "x3"};
      IloNumVar[] x       = model.numVarArray(3, lb, ub, varname);
      var[0] = x;

      double[] objvals = {1.0, 2.0, 3.0};
      model.addMaximize(model.scalProd(x, objvals));

      rng[0] = new IloRange[2];
      rng[0][0] = model.addLe(model.sum(model.prod(-1.0, x[0]),
                                        model.prod( 1.0, x[1]),
                                        model.prod( 1.0, x[2])), 20.0, "c1");
      rng[0][1] = model.addLe(model.sum(model.prod( 1.0, x[0]),
                                        model.prod(-3.0, x[1]),
                                        model.prod( 1.0, x[2])), 30.0, "c2");
   }

}








