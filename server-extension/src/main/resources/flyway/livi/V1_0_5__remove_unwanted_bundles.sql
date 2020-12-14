--delete publisher2
DELETE FROM oskari_appsetup_bundles
WHERE bundleinstance = 'publisher2' AND appsetup_id = (select id from oskari_appsetup where name = 'Default view' and application='servlet' AND page = 'index');

--delete myplaces
DELETE FROM oskari_appsetup_bundles
WHERE bundleinstance = 'myplaces2' AND appsetup_id = (select id from oskari_appsetup where name = 'Default view' and application='servlet' AND page = 'index');

--delete personaldata
DELETE FROM oskari_appsetup_bundles
WHERE bundleinstance = 'personaldata' AND appsetup_id = (select id from oskari_appsetup where name = 'Default view' and application='servlet' AND page = 'index');

--remove analyse
DELETE FROM oskari_appsetup_bundles
WHERE bundleinstance = 'analyse' AND appsetup_id = (select id from oskari_appsetup where name = 'Default view' and application='servlet' AND page = 'index');