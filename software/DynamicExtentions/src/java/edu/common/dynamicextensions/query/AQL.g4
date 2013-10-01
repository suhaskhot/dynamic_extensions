grammar AQL;

query         : (SELECT select_list WHERE)? filter_expr #QueryExpr
              ;
      
select_list   : arith_expr (',' arith_expr)*         #SelectExpr
              ;

filter_expr   : filter_expr AND filter_expr          #AndFilterExpr
              | filter_expr OR  filter_expr          #OrFilterExpr
              | filter_expr PAND filter_expr         #PandFilterExpr
              | LP filter_expr RP                    #ParensFilterExpr
              | NOT filter_expr                      #NotFilterExpr
              | filter                               #SimpleFilter
              ;
     
filter        : arith_expr  OP   arith_expr          #BasicFilter
              | arith_expr  MOP  literal_values      #MvFilter
              | FIELD       SOP  SLITERAL            #StringCompFilter
              | FIELD       EOP                      #ExistsFilter
              ;
              
literal_values: '(' literal (',' literal)* ')'
              ;
              
literal       : SLITERAL                             #StringLiteral 
              | INT                                  #IntLiteral
              | FLOAT                                #FloatLiteral
              | BOOL                                 #BoolLiteral
              ;                            
	 
arith_expr    : arith_expr ARITH_OP arith_expr       #ArithExpr
              | arith_expr ARITH_OP date_interval    #DateIntervalExpr
              | LP arith_expr RP                     #ParensArithExpr
              | MONTHS LP arith_expr RP              #MonthsDiffFunc
              | YEARS LP arith_expr RP               #YearsDiffFunc
              | FIELD                                #Field
              | literal                              #LiteralVal              
              ;	 
          
date_interval : YEAR MONTH? DAY?
              | YEAR? MONTH DAY?
              | YEAR? MONTH? DAY
              ;          
                   
               
WS       : [ \t\n\r]+ -> skip;

SELECT   : 'select';
WHERE    : 'where';
MONTHS   : 'months';
YEARS    : 'years';
OR       : 'or';
AND      : 'and';
PAND     : 'pand';
NOT      : 'not';
LP       : '(';
RP       : ')';
MOP      : ('in'|'not in');
SOP      : ('contains'|'starts with'|'ends with');
EOP      : ('exists'|'not exists');
OP       : ('>'|'<'|'>='|'<='|'='|'!='|'like');
INT      : '-'? DIGIT+;
FLOAT    : '-'? DIGIT+ '.' DIGIT+;
BOOL     : ('true'|'false');
YEAR     : DIGIT+ ('y'|'Y');
MONTH    : DIGIT+ ('m'|'M');
DAY      : DIGIT+ ('d'|'D');
DIGIT    : ('0'..'9');
ID       : ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'0'..'9'|'_')*;
FIELD    : (INT|ID) '.' ID ('.' ID)*;
SLITERAL : '"' SGUTS '"';
ESC      : '\\' ('\\' | '"');
ARITH_OP : ('+'|'-'|'*'|'/');

fragment
SGUTS    : (ESC | ~('\\' | '"'))*;
QUOTE    : '"';
