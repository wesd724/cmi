import { useState } from 'react';
import Box from '@mui/material/Box';
import Tab from '@mui/material/Tab';
import TabContext from '@mui/lab/TabContext';
import TabList from '@mui/lab/TabList';
import TabPanel from '@mui/lab/TabPanel';
import CommentTabs from './commentTabs';

const ActivityTabs = () => {
	const [value, setValue] = useState<string>('1');

	const handleChange = (event: React.SyntheticEvent, newValue: string) => {
		setValue(newValue);
	};

	return (
		<TabContext value={value}>
			<Box sx={{ width: "70vw", ml: "15%" }}>
				<TabList
					textColor="inherit" TabIndicatorProps={{
						style: {
							backgroundColor: "rgb(86, 81, 92)"
						}
					}} onChange={handleChange} centered>
					<Tab label="코인별 리뷰" value="1" />
				</TabList>
			</Box>
			<TabPanel sx={{ width: "70vw", ml: "15%", border: 1, borderColor: 'divider', padding: 0 }} value="1"><CommentTabs /></TabPanel>
		</TabContext>
	);
}

export default ActivityTabs;