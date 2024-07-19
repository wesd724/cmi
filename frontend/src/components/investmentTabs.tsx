import { useState } from 'react';
import Box from '@mui/material/Box';
import Tab from '@mui/material/Tab';
import TabContext from '@mui/lab/TabContext';
import TabList from '@mui/lab/TabList';
import TabPanel from '@mui/lab/TabPanel';
import Trade from './trade';
import UserAsset from './userAsset';

const InvestmentTabs = () => {
	const [value, setValue] = useState<string>('1');

	const handleChange = (event: React.SyntheticEvent, newValue: string) => {
		setValue(newValue);
	};

	return (
		<TabContext value={value}>
			<Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
				<TabList textColor="inherit" TabIndicatorProps={{
					style: {
						backgroundColor: "rgb(13, 99, 28)"
					}
				}} onChange={handleChange} centered>
					<Tab label="보유자산" value="1" />
					<Tab label="거래내역" value="2" />
				</TabList>
			</Box>
			<TabPanel sx={{ padding: 0 }} value="1"><UserAsset /></TabPanel>
			<TabPanel sx={{ padding: 0 }} value="2"><Trade /></TabPanel>
		</TabContext>
	);
}

export default InvestmentTabs;