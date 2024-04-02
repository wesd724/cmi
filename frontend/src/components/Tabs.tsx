import { useState } from 'react';
import Box from '@mui/material/Box';
import Tab from '@mui/material/Tab';
import TabContext from '@mui/lab/TabContext';
import TabList from '@mui/lab/TabList';
import TabPanel from '@mui/lab/TabPanel';
import Trade from './trade';

const Tabs = () => {
	const [value, setValue] = useState<string>('1');

	const handleChange = (event: React.SyntheticEvent, newValue: string) => {
		setValue(newValue);
	};

	return (
		<TabContext value={value}>
			<Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
				<TabList TabIndicatorProps={{
					style: {
						backgroundColor: "rgb(21, 105, 90)"
					  }
				}} onChange={handleChange} centered>
					<Tab label="보유자산" value="1" />
					<Tab label="거래내역" value="2" />
					<Tab label="Item Three" value="3" />
				</TabList>
			</Box>
			<TabPanel value="1">3</TabPanel>
			<TabPanel sx={{ padding: 0 }} value="2"><Trade /></TabPanel>
			<TabPanel value="3">Item Three</TabPanel>
		</TabContext>
	);
}

export default Tabs;