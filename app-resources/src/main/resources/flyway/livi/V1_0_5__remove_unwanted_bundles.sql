--delete publisher2
DELETE FROM portti_view_bundle_seq
WHERE bundleinstance = 'publisher2' AND view_id = (select id from portti_view where name = 'Default view' and application='servlet' AND page = 'index');

--delete myplaces
DELETE FROM portti_view_bundle_seq
WHERE bundleinstance = 'myplaces2' AND view_id = (select id from portti_view where name = 'Default view' and application='servlet' AND page = 'index');

--delete personaldata
DELETE FROM portti_view_bundle_seq
WHERE bundleinstance = 'personaldata' AND view_id = (select id from portti_view where name = 'Default view' and application='servlet' AND page = 'index');

--remove analyse
DELETE FROM portti_view_bundle_seq
WHERE bundleinstance = 'analyse' AND view_id = (select id from portti_view where name = 'Default view' and application='servlet' AND page = 'index');