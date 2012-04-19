
CREATE  PROCEDURE `curdemo`()
Begin  
   
declare entity_id integer ;
declare entity varchar(100);

declare cur1 cursor   for (select meta12.name,meta12.identifier from dyextn_association as11 join dyextn_attribute att11 on att11.identifier = as11.identifier
join dyextn_abstract_metadata meta11 on meta11.identifier = att11.entiy_id join dyextn_abstract_metadata meta12 on
meta12.identifier = as11.target_entity_id join  (
select meta2.name  targetEntityName from dyextn_abstract_metadata meta join dyextn_attribute attr
on meta.identifier = attr.entiy_id join   dyextn_association asso   on attr.identifier = asso.identifier
join dyextn_abstract_metadata meta2 on meta2.identifier =asso.target_entity_id and
meta.name like 'edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry'
group by meta2.name
having count(meta2.name)>1)temp1  on meta11.name like 'edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry'  and meta12.name = temp1.targetEntityName)
union
(select meta12.name,meta12.identifier from dyextn_association as11 join dyextn_attribute att11 on att11.identifier = as11.identifier
join dyextn_abstract_metadata meta11 on meta11.identifier = att11.entiy_id join dyextn_abstract_metadata meta12 on
meta12.identifier = as11.target_entity_id join  (
select meta2.name  targetEntityName from dyextn_abstract_metadata meta join dyextn_attribute attr
on meta.identifier = attr.entiy_id join   dyextn_association asso   on attr.identifier = asso.identifier
join dyextn_abstract_metadata meta2 on meta2.identifier =asso.target_entity_id and
meta.name like 'edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry'
group by meta2.name
having count(meta2.name)>1)temp1  on meta11.name like 'edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry'  and meta12.name = temp1.targetEntityName)
union
(select meta12.name,meta12.identifier from dyextn_association as11 join dyextn_attribute att11 on att11.identifier = as11.identifier
join dyextn_abstract_metadata meta11 on meta11.identifier = att11.entiy_id join dyextn_abstract_metadata meta12 on
meta12.identifier = as11.target_entity_id join  (
select meta2.name  targetEntityName from dyextn_abstract_metadata meta join dyextn_attribute attr
on meta.identifier = attr.entiy_id join   dyextn_association asso   on attr.identifier = asso.identifier
join dyextn_abstract_metadata meta2 on meta2.identifier =asso.target_entity_id and
meta.name like 'edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry'
group by meta2.name
having count(meta2.name)>1)temp1  on meta11.name like 'edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry'  and meta12.name = temp1.targetEntityName)
union
(select meta12.name,meta12.identifier from dyextn_association as11 join dyextn_attribute att11 on att11.identifier = as11.identifier
join dyextn_abstract_metadata meta11 on meta11.identifier = att11.entiy_id join dyextn_abstract_metadata meta12 on
meta12.identifier = as11.target_entity_id join  (
select meta2.name  targetEntityName from dyextn_abstract_metadata meta join dyextn_attribute attr
on meta.identifier = attr.entiy_id join   dyextn_association asso   on attr.identifier = asso.identifier
join dyextn_abstract_metadata meta2 on meta2.identifier =asso.target_entity_id and
meta.name like 'edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry'
group by meta2.name
having count(meta2.name)>1)temp1  on meta11.name like 'edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry'  and meta12.name = temp1.targetEntityName );

    open cur1;
    select 'open cursor';
    read_loop: LOOP

    fetch cur1 into entity,entity_id;
        update
            dyextn_abstract_metadata meta
            join dyextn_entity ent  on meta.identifier = ent.identifier
            join dyextn_entity_group eg on ent.entity_group_id = eg.identifier join
            dyextn_abstract_metadata meta2 on meta2.identifier = eg.identifier
            set meta.name=concat(meta2.name,meta.name)
            where meta.identifier = entity_id;
            commit;
    END LOOP;
    close cur1;  
 
end;

