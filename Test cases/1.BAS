SAMPLEFILE BASEDATA
PROJEKTS
  NrOfPro            :  2      & number of projects
  MinJob             :  12      & minimal number of jobs per project
  MaxJob             :  15      & maximal number of jobs per project
  MaxRelDate         :  0      & maximal release date
  DueDateFactor      : 0.5     & maximal due date
MODES
  MinMode            :  1      & minimal number of modes
  MaxMode            :  4      & maximal number of modes
  MinDur             :  1      & minimal duration
  MaxDur             :  10     & maximal duration
NETWORK
  MinOutSource       :  1      & minimal number of start activities per project
  MaxOutSource       :  3      & maximal number of start activities per project
  MaxOut             :  3      & maximal number of successor per activity
  MinInSink          :  1      & minimal number of end activities per project
  MaxInSink          :  2      & maximal number of end activities per project
  MaxIn              :  3      & maximal number of predecessors per activity
  Complexity         :  1.5    & complexity of network
RESSOURCEREQUEST/AVAILABILITY
  Rmin               :  2      & minimal number of renewable resources
  Rmax               :  2      & maximal number of renewable resources
  RminDemand         :  1      & minimal (per period) demand
  RmaxDemand         : 10      & maximal (per period) demand
  RRMin              :  1      & minimal number of resources requested
  RRMax              :  2      & maximal number of resources requested
  RRF                :  0.5    & resource factor
  RRS                :  0.2    & resource strength
  Number R-Func.     :  2
    p1               :  1.0    & probability to choose (duration) constant function
    p2               :  0.0    & probability to choose monotonically decreaseing function
  Nmin               :  2
  Nmax               :  2
  NminDemand         :  1
  NmaxDemand         :  10
  NRMin              :  1
  NRMax              :  2
  NRF                :  1.0
  NRS                :  0.7
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
  MinNofTaskG		 :2
  MaxNofTaskG		 :3  
  MinNofJobTask		 :3	  
  MaxNofJobTask		 :4
  MinNofQJob		 :1
  MaxNofQJob		 :3
  MinNofMJob		 :1		
  MaxNofMJob			:3		
SPECIAL RESOURCE
  NofSR				:3       
  SRA				:(1,d,o)
  SRB				:(2,nd,no)
  SRC				:(3,d,o)
  SRAmin			:3	
  SRAmax			:4			
  SRAminDemand		:(1)        
  SRAmaxDemand		:(5)		
  SRARmin			:1		
  SRARmax			:3		
  SRAF				:0.6		
  SRAS				:0.6 
  SRBmin			:5	
  SRBmax			:5			
  SRBminDemand		:(1,3)        
  SRBmaxDemand		:(5,8)		
  SRBRmin			:1			
  SRBRmax			:4			
  SRBF				:0.5		
  SRBS				:0.5
  SRCmin			:4	
  SRCmax			:6			
  SRCminDemand		:(1,1,1)        
  SRCmaxDemand		:(5,6,7)		 
  SRCRmin			:1			
  SRCRmax			:5		
  SRCF				:0.7		
  SRCS				:0.7 
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