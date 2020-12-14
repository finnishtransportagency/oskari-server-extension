--replace all bundleinstances of coordinatedisplay with coordinatetool
UPDATE oskari_appsetup_bundles set
	bundle_id = (SELECT id FROM oskari_bundle WHERE name='coordinatetool'),
	config =(SELECT config FROM oskari_bundle WHERE name='coordinatetool'),
	state = (SELECT state FROM oskari_bundle WHERE name='coordinatetool'),
	bundleinstance = 'coordinatetool'
WHERE
	bundle_id = (SELECT id FROM oskari_bundle WHERE name='coordinatedisplay');