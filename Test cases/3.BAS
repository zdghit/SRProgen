SAMPLEFILE BASEDATA
PROJEKTS
  NrOfPro            :  1      & number of projects
  MinJob             :  20      & minimal number of jobs per project
  MaxJob             :  20      & maximal number of jobs per project
  MaxRelDate         :  0      & maximal release date
  DueDateFactor      : 0.5     & maximal due date
MODES
  MinMode            :  1      & minimal number of modes
  MaxMode            :  1      & maximal number of modes
  MinDur             :  1      & minimal duration
  MaxDur             :  10     & maximal duration
NETWORK
  MinOutSource       :  1      & minimal number of start activities per project
  MaxOutSource       :  5      & maximal number of start activities per project
  MaxOut             :  4      & maximal number of successor per activity
  MinInSink          :  2      & minimal number of end activities per project
  MaxInSink          :  6      & maximal number of end activities per project
  MaxIn              :  6      & maximal number of predecessors per activity
  Complexity         :  1.5    & complexity of network
RESSOURCEREQUEST/AVAILABILITY
  Rmin               :  3      & minimal number of renewable resources
  Rmax               :  7      & maximal number of renewable resources
  RminDemand         :  1      & minimal (per period) demand
  RmaxDemand         : 10      & maximal (per period) demand
  RRMin              :  1      & minimal number of resources requested
  RRMax              :  3      & maximal number of resources requested
  RRF                :  0.5    & resource factor
  RRS                :  0.4    & resource strength
  Number R-Func.     :  2
    p1               :  0.2    & probability to choose (duration) constant function
    p2               :  0.8    & probability to choose monotonically decreaseing function
  Nmin               :  4
  Nmax               :  6
  NminDemand         :  1
  NmaxDemand         :  10
  NRMin              :  1
  NRMax              :  4
  NRF                :  1.0
  NRS                :  0.8
  Number N-Func.     :  2
    p1               :  0.0
    p2               :  1.0
  Dmin               :  0
  Dmax               :  0
  DminDemand         :  0
  DmaxDemand         :  0
  DRMin              :  0
  DRMax              :  0
  DRF                :  0.0
  DRST               :  0.0
  DRSP               :  0.0
  Number D-Func.     :  2
    p1               :  1.0
    p2               :  0.0
TASKGROUP
  MinNofTaskG		 :20
  MaxNofTaskG		 :20  
  MinNofJobTask		 :1	  
  MaxNofJobTask		 :1
  MinNofQJob		 :1
  MaxNofQJob		 :1
  MinNofMJob		 :1		
  MaxNofMJob		 :1		
SPECIAL RESOURCE
  NofSR				:1       
  SRA				:(2,d,no) 
  SRAmin			:1	
  SRAmax			:1			
  SRAminDemand		:(2,3)        
  SRAmaxDemand		:(9,9)		
  SRARmin			:1			
  SRARmax			:1			
  SRAF				:1		
  SRAS				:0.8
LIMIT OF ITERATIONS
  Tolerance Network  :  0.05  & tolerated network deviation
  Tolerance RF       :  0.05  & tolerated resource factor deviation
  MaxTrials          :  200   & maximal number of trials randomly gen.



FORMAT OF BASE FILE
  - a colon has to be followed by a value
  - only spaces are allowed between colon and value
  - a commend is allowed to follow a value
  - commends are allowed if there is no colon in
  - value and commend have to be seperated by space
  - value is integer with the exception of
    -> due date factor     -> complexity               -> resource factor
    -> resource strength   -> function probabilities
